package group6.com.cimenatime.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import group6.com.cimenatime.R;

/**
 * Created by HauDT on 05/2/2017.
 */
public class AboutFragment extends Fragment{

    private WebView webView1;
    private ProgressDialog progressDialog;

    public static String URL = "https://www.themoviedb.org/about/our-history";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.about_fragment, container, false);

        webView1 = (WebView) view.findViewById(R.id.webView1);
        webView1.getSettings().setJavaScriptEnabled(true);
        webView1.loadUrl(URL);
        setHasOptionsMenu(true);

        return view;
    }



    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menu_main_item_ChangeLayout).setVisible(false);
        menu.findItem(R.id.menu_main_item_MovieList).setVisible(true);
        menu.findItem(R.id.menu_main_item_Favorite).setVisible(true);
        menu.findItem(R.id.menu_main_item_About).setVisible(false);
        menu.findItem(R.id.menu_main_item_Setting).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }
}
