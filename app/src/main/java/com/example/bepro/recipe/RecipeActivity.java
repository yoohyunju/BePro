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
<<<<<<< Updated upstream
=======
import android.widget.Button;
import android.widget.EditText;
>>>>>>> Stashed changes

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bepro.MainActivity;
import com.example.bepro.R;

public class RecipeActivity extends Fragment {
    WebView webView;
<<<<<<< Updated upstream
    String url = "";
=======
    EditText editText;
    Button button;
    String url = "";
    String menu;
>>>>>>> Stashed changes
    // @NonNull : null 허용하지 않음
    // @Nullable : null 허용
    //onCreateView(): fragment가 자신의 UI를 처음으로 그릴 때 호출됨
    //fragment에 이벤트 정의하는 방법
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recipe, container, false);
<<<<<<< Updated upstream
        webViewSetting(v);
=======
        editText = v.findViewById(R.id.menu);
        button = v.findViewById(R.id.search);
        webView = v.findViewById(R.id.webview);
        webViewSetting(v);

        //앱 자체 검색창을 없애고 재료 쪽에서 넘어오는 것만 구현하는게 깔끔할듯.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu = editText.getText().toString();
                webView.loadUrl("https://www.10000recipe.com/recipe/list.html?q=" + menu); //웹뷰 url <수정>
            }
        });

>>>>>>> Stashed changes
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //This is the filter
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
    public void webViewSetting(View v){
        webView = (WebView) v.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true); //javaScript 허용
        webView.loadUrl("https://www.naver.com/"); //웹뷰 url <수정>
=======

    public void webViewSetting(View v){
        webView.getSettings().setJavaScriptEnabled(true); //javaScript 허용
        webView.loadUrl("https://www.10000recipe.com");
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

    //url 생성부
    public void createUrl(){

    }


=======
>>>>>>> Stashed changes
}
