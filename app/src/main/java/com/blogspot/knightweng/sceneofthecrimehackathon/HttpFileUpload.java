package com.blogspot.knightweng.sceneofthecrimehackathon;

import android.content.Entity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpFileUpload implements Runnable{
    URL connectURL;
    String murlString;
    //String Title;
    //String Description;
    byte[ ] dataToServer;
    FileInputStream fileInputStream = null;

    HttpFileUpload(String urlString){
        murlString = urlString;
        try{
            connectURL = new URL(urlString);
            //Title= vTitle;
            //Description = vDesc;
        }catch(Exception ex){
            Log.i("HttpFileUpload", "URL Malformatted");
        }
    }

    /*void SendNow(String urlFileName, FileInputStream fStream){
        fileInputStream = fStream;
        uploadFile(urlFileName);
    }*/
    void SendNow(String urlFileName)
    {
        uploadFile(urlFileName);
    }

    void uploadFile(String urlFileName)
    {
        File file = new File(urlFileName);
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            HttpPost httppost = new HttpPost(murlString);
            MultipartEntity mpEntity = new MultipartEntity();
            FileBody fileBody = new FileBody(file);
            mpEntity.addPart("fileToUpload", fileBody);

            httppost.setEntity(mpEntity);

            Log.d("Upload", "httppost.getRequestLine() : " + httppost.getRequestLine());
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity resEntity = response.getEntity();

            Log.d("Upload", "response.getStatusLine() :" + response.getStatusLine());

            if (resEntity != null) {
                Log.d("Upload", "EntityUtils.toString(resEntity) : " + EntityUtils.toString(resEntity));
            }
            if (resEntity != null) {
                resEntity.consumeContent();
            }

            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public int uploadFile(String sourceFileUri) {


        String fileName = sourceFileUri;
        int serverResponseCode = 0;

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        DataInputStream inStream = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(murlString);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("fileToUpload", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\"" + fileName +"\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile", "HTTP Response is : "
                        + serverResponseMessage + ": " + serverResponseCode);

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

                //------------------ read the SERVER RESPONSE
                try {
                    inStream = new DataInputStream( conn.getInputStream() );
                    String str;

                    while (( str = inStream.readLine()) != null)
                    {
                        Log.e("UploadFile","Server Response "+str);
                    }
                    inStream.close();

                }
                catch (IOException ioex){
                    Log.e("Debug", "error: " + ioex.getMessage(), ioex);
                }

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Upload file to server Exception", "Exception : "
                        + e.getMessage(), e);
            }
            return serverResponseCode;
    }*/

    /*void Sending(String urlFileName){
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="HttpFileUpload";
        try
        {
            Log.e(Tag,"Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection)connectURL.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("ENCTYPE", "multipart/form-data");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            conn.setRequestProperty("fileToUpload", urlFileName);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"fileToUpload\";filename=\"" + urlFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            Log.e(Tag,"Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            Log.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));

            // Responses from the server (code and message)
            int serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();
        }
        catch (MalformedURLException ex)
        {
            Log.e(Tag, "URL error: " + ex.getMessage(), ex);
        }

        catch (IOException ioe)
        {
            Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
        }
    }*/

    @Override
    public void run() {
        // TODO Auto-generated method stub
    }
}
