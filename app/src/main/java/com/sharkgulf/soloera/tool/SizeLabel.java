package com.sharkgulf.soloera.tool;

import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ClickableSpan;
import android.view.View;

import org.xml.sax.XMLReader;

import static com.trust.demo.basis.trust.utils.TrustAppUtils.getContext;

public class SizeLabel implements Html.TagHandler {    
    private int size;    
    private int startIndex = 0;    
    private int stopIndex = 0;
    private int mColor;
    private int click = 0;
    private HtmlOnClickListener mHtmlOnClickListener;
    public SizeLabel(int size) {        
        this.size = size;    
    }

    public SizeLabel(int color,HtmlOnClickListener htmlOnClickListener) {
        this.mColor = color;
        mHtmlOnClickListener = htmlOnClickListener;
    }

    @Override    
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {        
       if(tag.toLowerCase().equals("size")) {            
           if(opening){                
               startIndex = output.length();            
           }else{                
               stopIndex = output.length();                
               output.setSpan(new AbsoluteSizeSpan(dip2px(size)), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);           
          }        
       }else if(tag.toLowerCase().equals("click")){
           if(opening){
               startIndex = output.length();
           }else{
               stopIndex = output.length();
               output.setSpan(new ClickableImage((click++)+"",mColor,mHtmlOnClickListener), startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           }

       }
    }

   public static int dip2px(float dpValue) {    
       final float scale = getContext().getResources().getDisplayMetrics().density;    
      return (int) (dpValue * scale + 0.5f);
   }


    private class ClickableImage extends ClickableSpan {
        private String mAction;
        private int mColor;
        private HtmlOnClickListener mHtmlOnClickListener;
        public ClickableImage(String action,int color,HtmlOnClickListener onClickListener) {
            mAction = action;
            mColor = color;
            mHtmlOnClickListener = onClickListener;
        }

        @Override
        public void onClick(View widget) {
            mHtmlOnClickListener.OnClickListener(mAction);
        }


        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(mColor);
            ds.setUnderlineText(false);
            ds.clearShadowLayer();
        }
    }



    public interface HtmlOnClickListener{
        public void OnClickListener(String action);
    }

}