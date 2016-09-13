package ru.mos.school.exercise;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import rx.Observable;
import rx.Subscriber;
import rx.observers.Subscribers;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RxExercise
{


    public static void main(String[] args) throws Exception
    {
        Request request = new Request();
        request.addParam("me_1");
        request.addParam("me_2");
        request.addParam("me_3");

        request.addParam("uri1_1");
        request.addParam("uri1_2");

        request.addParam("uri2_1");

        //TestSubscriber<Response> subscriber = TestSubscriber.create(Subscribers.create(s -> System.out.println("Result: " + s)));
        Subscriber<Response> subscriber = Subscribers.create(s -> System.out.println("Result: " + s));
        createObservable(request).subscribe(subscriber);
        //subscriber.awaitTerminalEvent();

        Thread.sleep(10000);
    }

    private static Observable<Response> createObservable(Request request)
    {
        Multimap<String, String> routes = ArrayListMultimap.create();
        for (String param : request.getParams())
        {
            if (param.startsWith("me"))
            {
                routes.put("me", param);
            }
            else if (param.startsWith("uri1"))
            {
                routes.put("uri1", param);
            }
            else if (param.startsWith("uri2"))
            {
                routes.put("uri2", param);
            }
        }

        List<Observable<Map.Entry<String, String>>> observables = new ArrayList<>();
        for (Map.Entry<String, Collection<String>> entry : routes.asMap().entrySet())
        {
            observables.add(createRequest(entry.getKey(), entry.getValue()));
        }
        return Observable.merge(observables).reduce(new Response(), (response, entry) -> {
            response.addParam(entry.getKey(), entry.getValue());
            //logThread("adding param " + entry.getKey());
            return response;
        });
    }

    private static Observable<Map.Entry<String, String>> createRequest(String uri, Collection<String> requestedParams)
    {
        Observable<Response> responseObservable = Observable.create(subscriber -> {
            subscriber.onNext(getResponse(uri, requestedParams));
            subscriber.onCompleted();
        });

        return responseObservable.subscribeOn(Schedulers.io())
                .map(Response::getParams)
                .map(Map::entrySet)
                .flatMap(Observable::from);
    }

    private static Response getResponse(String uri, Collection<String> requestedParams)
    {
        logThread("URI: " + uri);
        Response response = new Response();
        for (String param : requestedParams)
        {
            response.addParam(param, param + "'s value");
        }
        if (!"me".equals(uri))
        {
            sleep();
        }
        logThread("Sleep time's over!");
        return response;
    }

    private static void logThread(Object message)
    {
        System.out.println(message + ", thread: " + Thread.currentThread().getName());
    }

    private static void sleep()
    {
        try
        {
            Thread.sleep((long) (1000 + Math.random() * 5000d));
        }
        catch (InterruptedException ex)
        {
        }
    }
}
