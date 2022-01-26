package com.belicoffee.WebRTC;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.SurfaceHolder;

public class MySurfaceView extends GLSurfaceView {
    MyRenderer mRenderer;

    public MySurfaceView(Context context) {
        super(context);
        mRenderer = new MyRenderer(this);
        setEGLContextClientVersion(2);
        setRenderer(mRenderer);
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        mRenderer.close();
        super.surfaceDestroyed(holder);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
    }
}


