package com.example.bepro.recipe;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepro.MainActivity;
import com.example.bepro.R;

public class RecipeActivity extends Fragment {
    WebView webView;
    String menu;
    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    //fragment에 이벤트 정의하는 방법

    public RecipeActivity() {
    }

    public RecipeActivity(String menu) {
        this.menu = menu;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe, container, false);
        webView = v.findViewById(R.id.webview);
        webViewSetting(v);

        if(menu != null){
            //품목에서 넘어올 경우
            webView.loadUrl("https://www.10000recipe.com/recipe/list.html?q=" + menu);
        }

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        ((MainActivity)getActivity()).onBackPressed();
                    }
                    return true;
                }
                return false;
            }
        });
        return v;
    }

    public void webViewSetting(View v){
        webView.getSettings().setJavaScriptEnabled(true); //javaScript 허용
        webView.loadUrl("https://www.10000recipe.com");
        webView.setWebChromeClient((new WebChromeClient())); //웹뷰에서 크롬 허용
        webView.setWebViewClient(new webViewClientClass());//새창 띄우기 없이 웹뷰 내에서 작동
    }

    //웹뷰 내에서 페이지 이동 클래스
    private class webViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("check URL", url);
            view.loadUrl(url);
            return true;
        }
    }
}
