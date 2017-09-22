package com.yqq.rxjavastudy;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 参考文章 ：https://gank.io/post/560e15be2dca930e00da1083   给 Android 开发者的 RxJava 详解
 */
public class MainActivity extends AppCompatActivity {


    private static final String TAG ="MainActivity" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void doHello(View view){
            Observable.create(new ObservableOnSubscribe<String>() {


                @Override
                public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                    //事件源产生发送事件
                    e.onNext("事件1");
                    e.onNext("事件2");
                    e.onNext("事件3");
                    //RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志。
                    e.onComplete();
//                    onError(): 事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出。
//                    在一个正确运行的事件序列中, onCompleted() 和 onError() 有且只有一个，并且是事件序列中的最后一个。需要注意的是，
//                    onCompleted() 和 onError()
//                    二者也是互斥的，即在队列中调用了其中一个，就不应该再调用另一个。
                   // e.onError(new NullPointerException());
                }
                //事件源订阅观察者?
            }).subscribe(new Observer<String>(){

                @Override
                public void onSubscribe(@NonNull Disposable d) {

                    Log.e(TAG," onSubscribe----->"+d.isDisposed());
                }

                @Override
                public void onNext(@NonNull String s) {
                    //观察者接收事件
                    Log.e(TAG,"onNext----->"+s);
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.e(TAG," onError----->");
                    e.printStackTrace();
                }

                @Override
                public void onComplete() {
                    Log.e(TAG," onComplete----->");

                }
            });



    }

    public void doJust(View v){
        //这种方式是自己调用onComplete方法
       // just(T...): 将传入的参数依次发送出来
        Observable.just("1","2","3").subscribe(new Observer<String>() {
                                                   @Override
                                                   public void onSubscribe(@NonNull Disposable d) {
                                                       Log.e(TAG," Just onSubscribe----->"+d.isDisposed());
                                                   }

                                                   @Override
                                                   public void onNext(@NonNull String s) {
                                                       Log.e(TAG," Just onNext----->"+s);
                                                   }

                                                   @Override
                                                   public void onError(@NonNull Throwable e) {
                                                       Log.e(TAG," Just onError----->"+e);
                                                       e.printStackTrace();
                                                   }

                                                   @Override
                                                   public void onComplete() {
                                                       Log.e(TAG," Just onComplete----->");
                                                   }
                                               }
          );

    }


    //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
    public void doFrom(View view){
        User[] words = {new User().setmAge("111").setmName("111"), new User().setmAge("222").setmName("222"),new User().setmAge("333").setmName("333")};
        Observable.fromArray(words).subscribe(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull User user) {
                Log.e(TAG,"onNext=====user===="+user.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }








    //传递对象
    public void doPassObject(View view){
        //from(T[]) / from(Iterable<? extends T>) : 将传入的数组或 Iterable 拆分成具体对象后，依次发送出来。
        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<User> e) throws Exception {
                User user=new User();
                user.mName="测试对象名";
                user.mAge="测试者年级";
                Log.e(TAG,"subscribe=====user===="+user);
                e.onNext(user);
                e.onComplete();

            }
        }).subscribe(new Observer<User>(){

            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull User user) {

                Log.e(TAG,"onNext=====user===="+user.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });



    }




    //
    public void doThreadControll(View v){


        //Schedulers.immediate(): 直接在当前线程运行，相当于不指定线程。这是默认的 Scheduler。
        //Schedulers.newThread(): 总是启用新线程，并在新线程执行操作。
        //Schedulers.io(): I/O 操作（读写文件、读写数据库、网络信息交互等）所使用的 Scheduler。
        // 行为模式和 newThread() 差不多，区别在于 io() 的内部实现是是用一个无数量上限的线程池，可以重用空闲的线程，
        // 因此多数情况下 io() 比 newThread() 更有效率。
        // 不要把计算工作放在 io() 中，可以避免创建不必要的线程。
        //Schedulers.computation(): 计算所使用的 Scheduler。这个计算指的是 CPU 密集型计算，
        // 即不会被 I/O 等操作限制性能的操作，例如图形的计算。这个 Scheduler 使用的固定的线程池，
        // 大小为 CPU 核数。不要把 I/O 操作放在 computation() 中，否则 I/O 操作的等待时间会浪费 CPU。
        //Android 还有一个专用的 AndroidSchedulers.mainThread()，它指定的操作将在 Android 主线程运行。
        Observable.create(new ObservableOnSubscribe<String>() {


            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Log.e(TAG,"subscribe运行线程==="+Thread.currentThread().getName());
                    e.onNext("1");
                    e.onNext("2");
                    e.onNext("3");
                e.onComplete();


            }
        }).subscribeOn(Schedulers.io())// 指定 subscribe() 发生在 IO 线程
        .observeOn(AndroidSchedulers.mainThread())// 指定 Subscriber 的回调发生在主线程
        .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.e(TAG,"onSubscribe===");
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.e(TAG,"onNext运行线程==="+Thread.currentThread().getName());
                Log.e(TAG,"onNext回调结果==="+s);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {
                Log.e(TAG,"onComplete===");
            }
        });


  }






  public void doMap(View view){
      //map一对一的变换
      Observable.just("小明").map(new Function<String, User>() {

          @Override
          public User apply(@NonNull String s) throws Exception {
              Log.e(TAG,"map变换=="+s);
              Log.e(TAG,"map变换==apply线程"+Thread.currentThread().getName());

              return new User().setmName(s);
          }
      }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<User>() {
          @Override
          public void onSubscribe(@NonNull Disposable d) {

          }

          @Override
          public void onNext(@NonNull User user) {
              Log.e(TAG,"map变换==onNext====线程"+Thread.currentThread().getName());

              Log.e(TAG,"map变换==onNext====user"+user);


          }

          @Override
          public void onError(@NonNull Throwable e) {

          }

          @Override
          public void onComplete() {

          }
      });


  }



  public void doflatMap(View view){
      //1对多

      EducationBackgroud e1=new EducationBackgroud();
      e1.vals=new String[]{"A小学","A中心小学","A初中","A县高中","A大学"};
      EducationBackgroud e2=new EducationBackgroud();
      e2.vals=new String[]{"B小学","B中心小学","B初中","B市高中","B大学","B大学"};
      EducationBackgroud e3=new EducationBackgroud();
      e3.vals=new String[]{"C小学","C中心小学","C初中"};


      User[] words = {new User().setmAge("21").setmName("李华").setmEducationBackgroud(e1), new User().setmAge("21").setmName("小明").setmEducationBackgroud(e3),new User().setmAge("25").setmName("大神").setmEducationBackgroud(e2)};

      Observable.fromArray(words).observeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).flatMap(new Function<User, ObservableSource<EducationBackgroud>>() {

          @Override
          public ObservableSource<EducationBackgroud> apply(@NonNull User user) throws Exception {
                //事件转换
              return Observable.just(user.mEducationBackgroud);
          }
      }).subscribe(new Observer<EducationBackgroud>() {
          @Override
          public void onSubscribe(@NonNull Disposable d) {

          }

          @Override
          public void onNext(@NonNull EducationBackgroud educationBackgroud) {

              for(int i=0,N=educationBackgroud.vals.length;i<N;i++){

                  Log.e(TAG,"flatMap====onNext()"+educationBackgroud.vals[i]);
              }

          }

          @Override
          public void onError(@NonNull Throwable e) {

          }

          @Override
          public void onComplete() {
              Log.e(TAG,"onComplete");
          }
      });




  }



public void doManyThreadSeitch(View view){

    Observable.just("测试").subscribeOn(Schedulers.io()).observeOn(Schedulers.newThread()).map(new Function<String, User>() {
        @Override
        public User apply(@NonNull String s) throws Exception {
            Log.e(TAG,"所在线程1----"+Thread.currentThread().getName());
            return new User().setmName(s);
        }
    }).observeOn(Schedulers.io()).map(new Function<User, User>() {
        @Override
        public User apply(@NonNull User user) throws Exception {
            Log.e(TAG,"所在线程2----"+Thread.currentThread().getName());
            return user.setmAge("20");
        }
    }).observeOn(Schedulers.computation()).subscribe(new Observer<User>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            Log.e(TAG,"所在线程onSubscribe----"+Thread.currentThread().getName());
        }

        @Override
        public void onNext(@NonNull User user) {
            Log.e(TAG,"onNext----"+Thread.currentThread().getName()+"\n"+user);
        }

        @Override
        public void onError(@NonNull Throwable e) {

        }

        @Override
        public void onComplete() {
            Log.e(TAG,"onComplete----"+Thread.currentThread().getName());
        }
    });



}
















}
