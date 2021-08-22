
package com.jadblack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

public final class ImageFunctions {
	public static final String tag = "ImageFunctions";
	
	public static Bitmap getImageFromUrl(String url){
		ErrorHandler.getInstance().clean();
		
		String completeUrl = "http://" + url;
		URL bitmapUrl;
		try {
			bitmapUrl = new URL(completeUrl);
			HttpURLConnection conn = (HttpURLConnection) bitmapUrl.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream input = conn.getInputStream();
			Bitmap bitmapImage = BitmapFactory.decodeStream(input);
			return bitmapImage;
		}catch(Exception e){
			ErrorHandler.getInstance().setErrorMessage("Error loading image: " + e);
			return null;
		}
	}
	
	public static boolean saveImageToFile(String filePath, String fileExtension, Bitmap image){
		ErrorHandler.getInstance().clean();
		
		try {
			File f = new File(filePath);
			FileOutputStream out = new FileOutputStream(f);
			//JPEG or JPG
			if(fileExtension.equalsIgnoreCase("JPG") || fileExtension.equalsIgnoreCase("JPEG")){
				image.compress(Bitmap.CompressFormat.JPEG, 100, out);
			}
			//PNG
			if(fileExtension.equalsIgnoreCase("PNG")){
				image.compress(Bitmap.CompressFormat.PNG, 100, out);
			}
				
			out.flush();
			out.close();
			
			Log.d(tag, "Image Saved on " + f.getAbsolutePath());
			
			return true;
		} catch (Exception e){
			ErrorHandler.getInstance().setErrorMessage("Error saving image to file: " + e);
			Log.d(tag, "Error: " + e);
			return false;
		}
	}
	
	public static Bitmap getImageFromFile(String filePath){
		ErrorHandler.getInstance().clean();
		
		Bitmap b = BitmapFactory.decodeFile(filePath);
		if( b == null ){
			ErrorHandler.getInstance().setErrorMessage("Error loading image from file");
		}
		return b; 
	}
	
	public static Bitmap getScaledImage(Bitmap b, int width, int height, boolean filter){
		ErrorHandler.getInstance().clean();
		
		if(b == null){
			ErrorHandler.getInstance().setErrorMessage("Error scaling image");
			return null;
		}
		return Bitmap.createScaledBitmap(b, width, height, filter);		
	}
	
	public static String getImageBase64(Bitmap b){
		ErrorHandler.getInstance().clean();
		
		byte[] input = imageToByteArray(b);
		if( input == null ){
			ErrorHandler.getInstance().setErrorMessage("Error converting image to base64");
			return "";		
		}
		return Base64.encodeToString(input, Base64.DEFAULT);
	}
	
	public static byte[] imageToByteArray(Bitmap b){
		if( b == null ) return null;
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
		byte[] byteArray = stream.toByteArray();
		return byteArray;
	}


	// NEW IMAGE COMPRESSION
	public static String getCompressed64Imagev2(String imageUrl)
	{
		String compressedBase64ImageString = "";

		//Log.d(TAG, "Metodo compressImage:::: URL: " + imageUrl);

		HttpURLConnection conn = null;
		Bitmap b = null;
		InputStream is = null;

		try{

			// Llamo metodo para que me decodifique la imagen con tamaño adecuado para la memoria del dispositivo.
			b = decodeSampledBitmapFromImageUrl(imageUrl);

			// Comprimiendo
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			// Comprimo imagen
			b.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);

			byte[] byteArray = byteArrayOutputStream.toByteArray();

			compressedBase64ImageString = Base64.encodeToString(byteArray, Base64.DEFAULT);

			return compressedBase64ImageString;

		} catch (Exception e){
			return "";
		}
	}



	// ----------------------------------------------------------------------------------------------
	// ---- MAIN IMAGE COMPRESSION METHOD
	// ----------------------------------------------------------------------------------------------
	public static Bitmap decodeSampledBitmapFromImageUrl(String imageUrl) {

		// Si falla la compresion de la documentacion de Google, entonces hago la compresion de respaldo que intenta hacer varias veces
		Bitmap bitmapImage = null;

		try{
			Log.d("IMAGE_COMPRESSION","Executing Inalambrik MAIN Compression method.");

			// Creo Options para el BitmapImage.
			final BitmapFactory.Options options = new BitmapFactory.Options();

			// Activo en las options, que por ahora solo decodifique los bordes de la imagen.
			// Esto para conocer las dimensiones de la imagen original.
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(imageUrl, options);

			// Esto se hace para evitar una division sobre cero.
			if (options.outWidth > 0 && options.outHeight > 0)
			{
				int reqWidth = 600; // Fijo un width fijo.
				int reqHeight = (int) (options.outHeight * reqWidth / (double) options.outWidth); // Para mantener el aspect ratio original de la imagen.

				// Calculo el SampleSize adecuado con el que se codificara la imagen original,
				// para poder codificarla a un tamaño aproximado al deseado.
				options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
			}
			else
			{
				options.inSampleSize = calculateInSampleSize(options, 600, 800);
			}

			// Desactivo en las opciones del Bitmap para que ahora se decodifique todo el contenido de la imagen (y no solo sus bordes).
			options.inJustDecodeBounds = false;

			// Ahora el Bitmap se decodificar con el Sample Size calculado anteriormente.
			bitmapImage = BitmapFactory.decodeFile(imageUrl, options);


		} catch(Exception e)
		{
			// Si falla por memoria la compresion principal. Llamo un metodo que reintenta varias veces
			// con diferentes sampleSizes hasta que pueda comprimir la imagen.
			Log.d("IMAGE_COMPRESSION","Main Compression Method Fails. Executing Inalambrik BACKUP Compression method.");
			bitmapImage = customDecodeFile(imageUrl);
		}
		return bitmapImage;
	}


	// ----------------------------------------------------------------------------------------------
	// ---- Metodo para calcular SmapleSize (para MAIN IMAGE COMPRESSION METHOD - Metodo Principal de Compresion)
	// ----------------------------------------------------------------------------------------------
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	// ---------------------------------------------------------------------------------------
	// ---- BACKUP COMPRESSION METHOD
	// ---------------------------------------------------------------------------------------
	// Si falla por algo la compresion. Entonces llamo otro metodo, que por "prueba y error" reintenta comprimir la imagen
	// usando varios sampleSizes hasta que el dispositivo puedo decodificarla.
	public static Bitmap customDecodeFile(String pathName) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
			try {
				bitmap = BitmapFactory.decodeFile(pathName, options);
				Log.d("IMAGE_COMPRESSION", "BACKUP COMPRESSION MATHOD: Decoded successfully for sampleSize " + options.inSampleSize);
				break;
			} catch (OutOfMemoryError outOfMemoryError) {
				// If an OutOfMemoryError occurred, we continue with for loop and next inSampleSize value
				Log.e("IMAGE_COMPRESSION", "BACKUP COMPRESSION MATHOD: outOfMemoryError while reading file for sampleSize " + options.inSampleSize + " retrying with higher value");
			}
		}
		return bitmap;
	}
}
