package com.slidenerd.domparsingexample;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    PlaceHolderFragment taskFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this file pushed to github
        super.onCreate(savedInstanceState);


        try {
            // Google Play will install latest OpenSSL
           // ProviderInstaller.installIfNeeded(getApplicationContext());
            SSLContext sslContext;
            sslContext = SSLContext.getInstance("TLSv1.2");
            sslContext.init(null, null, null);
            sslContext.createSSLEngine();
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }


        setContentView(R.layout.activity_main);

        if (savedInstanceState==null)
        {
            taskFragment=new PlaceHolderFragment();
            getSupportFragmentManager().beginTransaction().add(taskFragment,"MyFragment").commit();
        }
        else
        {
            taskFragment= (PlaceHolderFragment) getSupportFragmentManager().findFragmentByTag("MyFragment");
        }
        taskFragment.stastTask();
    }
    public static class PlaceHolderFragment extends Fragment
    {
        TechCrunchTask downloadTask;

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            setRetainInstance(true);
        }
        public void stastTask()
        {
            if(downloadTask!=null)
            {
                downloadTask.cancel(true);
            }
            else
            {
                downloadTask=new TechCrunchTask();
                downloadTask.execute();
            }
        }
    }

    static class  TechCrunchTask extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            String downLoadURL="https://feeds.feedburner.com/techcrunch/android?format=xml";
            try {
                URL url=new URL(downLoadURL);
                L.m("do in background");
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream=connection.getInputStream();
                processXML(inputStream);
            } catch (MalformedURLException e) {
               e.printStackTrace();
            } catch (IOException e) {
                L.m(""+e);
            } catch (SAXException e) {
                L.m(""+e);
            } catch (ParserConfigurationException e) {
                L.m(""+e);
            }
            return null;
        }
        public void processXML(InputStream inputStream) throws ParserConfigurationException, IOException, SAXException {

            Node currentItem=null,currentChild=null;
            NodeList itemClidren=null;

            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document xmlDocument = documentBuilder.parse(inputStream);
            Element rootElement=xmlDocument.getDocumentElement();
            L.m(""+rootElement.getTagName());
            NodeList itemList=rootElement.getElementsByTagName("item");
            for (int i=0;i<itemList.getLength();i++)
            {
               currentItem= itemList.item(i);
               itemClidren=currentItem.getChildNodes();
               //L.m("inside i forloop");

            for (int j=0;j<itemClidren.getLength();j++)
            {
                currentChild=itemClidren.item(j);
                //L.m("inside j forloop");
                int count=0;
                if (currentChild.getNodeName().equalsIgnoreCase("image"))
                {
                    L.m("inside if condition");
                    count++;
                    if (count==2)
                    {
                        L.m(currentChild.getAttributes().item(0).getTextContent());
                    }
                }
                count=0;
            }
            }
        }
    }

}
