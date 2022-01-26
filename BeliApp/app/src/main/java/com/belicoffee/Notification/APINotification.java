package com.belicoffee.Notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APINotification {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAArOvOFOI:APA91bGvSEbjNZBLWyRFlpusgCi438wjcZhonilnyXIW7964K04QQ36iH3RsOkShe3eKg4aVSwQWDjCt1FiJqc7rphh8SSzD7IYRPjmNJK90XRlEkJUQtuvq5UMOyp6eUJ9jV-qb_Qc9"
            }
    )

    @POST("fcm/send")
    Call<Response> sendNotification(@Body ChatNotification chatNotification);
}
