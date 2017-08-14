package com.example.diogo.discoverytrip.Util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diogo.discoverytrip.R;
import com.example.diogo.discoverytrip.REST.ApiClient;
import com.example.diogo.discoverytrip.REST.ServerResponses.ErrorResponse;
import com.example.diogo.discoverytrip.REST.ServerResponses.Item;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import okhttp3.ResponseBody;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by renato on 07/02/17.
 */
public class ListAdapterOferta extends ArrayAdapter<Item>{
    private List<View> views;
    private LayoutInflater inflater;
    private List<Item> ofertas;
    private Activity context;
    private SimpleDateFormat serverDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private SimpleDateFormat nomalDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private static Semaphore semaphore = new Semaphore(1);
    final static Handler handler = new Handler();

    public ListAdapterOferta(Activity context, List<Item> itens){
        super(context, R.layout.item_oferta,itens);

        this.inflater = context.getLayoutInflater();
        this.ofertas = itens;
        this.context = context;
        this.views = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(views.size() > position){
            return views.get(position);
        }

        Log.d("Logger","getView "+position);
        final Item oferta = ofertas.get(position);
        View view = inflater.inflate(R.layout.item_oferta, null, true);
        final ImageView foto = (ImageView) view.findViewById(R.id.iten_img);

        final TextView titulo  = (TextView) view.findViewById(R.id.iten_name);

        titulo.setText(oferta.getName());
        Log.d("Logger","Item "+oferta.getName());


        new Thread(new Runnable() {
            @Override
            public void run() {
                loadImage(foto, oferta.getId(), context);
            }
        }).start();


        views.add(view);
        return view;
    }

   public static void loadImage(final ImageView imgView, final String itemId, final Context context){
        try{
            semaphore.acquire();
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("Logger","loadImage");
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    imgView.setImageBitmap(loadImageFromInternalStorage(itemId,context));
                    Log.d("Looger","loadImage finish");
                    semaphore.release();

                } catch (FileNotFoundException e) {
                    retrofit2.Call<ResponseBody> call = ApiClient.API_SERVICE.downloadImage(itemId);
                    call.enqueue(new retrofit2.Callback<ResponseBody>() {

                        @Override
                        public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                InputStream input = response.body().byteStream();
                                //Convert a foto em Bitmap
                                final Bitmap img = BitmapFactory.decodeStream(input);

                                saveImageToInternalStorage(itemId,img,context);

                                //Coloca a foto na imageView
                                try {
                                    imgView.setImageBitmap(loadImageFromInternalStorage(itemId,context));
                                } catch (FileNotFoundException e1) {
                                    Log.e("Logger","Erro ao carregar foto");
                                    e1.printStackTrace();
                                }

                            } else {
                                try {
                                    ErrorResponse error = ApiClient.errorBodyConverter.convert(response.errorBody());
                                    Log.e("Pesquisa pontos tur",error.getErrorDescription());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            semaphore.release();
                            Log.d("Logger","Load image finish");
                        }

                        @Override
                        public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
                            // Log error here since request failed
                            Log.e("Pesquisa pontos tur","Erro ao baixar imagem");
                            semaphore.release();
                        }
                    });
                }
            }
        });
    }

    private static void saveImageToInternalStorage(String imageName, Bitmap imagem, Context context){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        imagem.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        try{
            FileOutputStream fos = context.openFileOutput(imageName,MODE_PRIVATE);
            fos.write(byteArray);
            fos.close();
        }catch (IOException e) {
            Log.w("InternalStorage", "Error writing", e);
        }
    }

    private static Bitmap loadImageFromInternalStorage(String imageName, Context context) throws FileNotFoundException {

        FileInputStream fis = context.openFileInput(imageName);
        try {
            byte[] imageBytes = new byte[(int) fis.getChannel().size()];
            fis.read(imageBytes,0, (int) fis.getChannel().size());
            fis.close();
            Log.d("Logger","Image file "+imageBytes);
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBytes,0,imageBytes.length,null);
            return imageBitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public Item getItem(int position){
        return ofertas.get(position);
    }
}