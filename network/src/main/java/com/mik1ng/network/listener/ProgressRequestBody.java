package com.mik1ng.network.listener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;

/**
 */
public class ProgressRequestBody extends RequestBody {

    public interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

    public RequestBody getRequestBody() {
        return requestBody;
    }

    /**
     * 实际的待包装请求体
     */
    private final RequestBody requestBody;

    /**
     * 进度回调接口
     */
    private final ProgressRequestListener progressListener;

    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink bufferedSink;

    /**
     * 构造函数，赋值
     *
     * @param requestBody      待包装的请求体
     * @param progressListener 回调接口
     */
    public ProgressRequestBody(RequestBody requestBody, ProgressRequestListener progressListener) {
        this.requestBody = requestBody;
        this.progressListener = progressListener;
    }

    /**
     * 重写调用实际的响应体的contentType
     *
     * @return MediaType
     */
    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    /**
     * 重写调用实际的响应体的contentLength
     *
     * @return contentLength
     * @throws IOException 异常
     */
    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    /**
     * 重写进行写入
     *
     * @param sink BufferedSink
     * @throws IOException 异常
     */
    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (sink instanceof Buffer){
            //因为项目重写了日志拦截器，而日志拦截器里面调用了 RequestBody.writeTo方法，但是 它的sink类型是Buffer类型，所以直接写入
            //如果不这么做的话，上传进度最终会达到200%，因为被调用2次，而且日志拦截的writeTo是直接写入到 buffer 对象中，所以会很快；
            requestBody.writeTo(sink);
            return;
        }
        // JakeWharton's issues
        final long totalBytes = contentLength();
        BufferedSink progressSink = Okio.buffer(new ForwardingSink(sink) {
            private long bytesWritten = 0L;

            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                bytesWritten += byteCount;
                if (progressListener != null) {
                    progressListener.onRequestProgress(bytesWritten, totalBytes, bytesWritten == totalBytes);
                }
                super.write(source, byteCount);
            }
        });
        requestBody.writeTo(progressSink);
        progressSink.flush();


    }

}
