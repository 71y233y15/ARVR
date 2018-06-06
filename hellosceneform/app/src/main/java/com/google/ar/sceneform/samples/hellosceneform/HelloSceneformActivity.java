/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.app.Application;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Plane.Type;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.security.PublicKey;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {
  private static final String TAG = HelloSceneformActivity.class.getSimpleName();

  private ArFragment arFragment;
  private ModelRenderable HouseRenderable;

  //ImageButton imageButton; //按鈕型的設定圖片
  private DrawerLayout drawerLayout;//宣告點擊事件需要的變數
  private NavigationView navigation_view;




    //建立共用變數類別
     //private GlobalVariable gv;

    @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  // CompletableFuture requires api level 24
  // FutureReturnValueIgnored is not valid
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_ux);


      ImageView imageView = (ImageView) findViewById(R.id.imageView);//讀取imageview的指令

      drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);//點擊事件需要的原件在哪
      navigation_view = (NavigationView) findViewById(R.id.nav_view);

        Runnable my_Run = new Runnable(){
            @Override
            public void run() {
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        };
      //gv.setBuildpic("@drawable/ic_launcher");//圖片

      // 為navigatin_view設置點擊事件
      navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {

              // 點選時收起選單
              drawerLayout.removeCallbacks(my_Run);
              drawerLayout.postDelayed( my_Run, 5000);

              // 取得選項id
              int id = item.getItemId();
              int Select_flag = 0;
              //預設
              String buildstr = "@drawable/ic_launcher";
              String HouseString = "andy";
              int RawID;
              int imageResource;
              // 依照id判斷點了哪個項目並做相應事件
              if (id == R.id.build1) {
                  // 按下「build1」要做的事
                  //Toast.makeText(HelloSceneformActivity.this, "Orange", Toast.LENGTH_SHORT).show();
                  //gv.setBuildpic("@drawable/ic_launcher2");
                  //以下這兩行原本是合在一起的，我把第2行移動到下方
                  HouseString = "@raw/orange";
                  //RawID = getResources().getIdentifier(HouseString , "id", "package.name");

                  buildstr = "@drawable/orange_icon";
                  Select_flag = 1;
              }
              else if (id == R.id.build2) {
                  // 按下「build2」要做的事
                  //Toast.makeText(HelloSceneformActivity.this, "Blue", Toast.LENGTH_SHORT).show();
                  HouseString = "@raw/blue";
                  //RawID = getResources().getIdentifier(HouseString , "id", "package.name");
                  buildstr = "@drawable/blue_icon";
                  Select_flag = 1;
              }
              else if (id == R.id.build3) {
                  // 按下「build3」要做的事
                  //Toast.makeText(HelloSceneformActivity.this, "Blue_Rock", Toast.LENGTH_SHORT).show();
                  HouseString = "@raw/blue_rock";
                  //RawID = getResources().getIdentifier(HouseString , "id", "package.name");
                  buildstr = "@drawable/blue_rock_icon";
                  Select_flag = 1;
              }
              else if (id == R.id.build4) {
                  // 按下「build4」要做的事
                  //Toast.makeText(HelloSceneformActivity.this, "Red", Toast.LENGTH_SHORT).show();
                  HouseString = "@raw/red";
                  //RawID = getResources().getIdentifier(HouseString , "id", "package.name");
                  buildstr = "@drawable/red_icon";
                  Select_flag = 1;
              }
              //設定要用哪個3D模型
              RawID = getResources().getIdentifier(HouseString, null, getPackageName());
              //設定圖片範例要呈現哪一張
              if(Select_flag ==1) {
                  imageResource = getResources().getIdentifier(buildstr, null, getPackageName());
                  Drawable image = getResources().getDrawable(imageResource);
                  imageView.setImageDrawable(image);
                  imageView.setVisibility(View.VISIBLE);
                  /*//延遲5秒
                  imageView.postDelayed(new Runnable() {
                      @Override
                      public void run() {
                          imageView.setVisibility(View.INVISIBLE);
                      }
                  }, 10000);*/


                  //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
                  //主要AR銀幕呈現的部分在這以下
                  arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

                  // When you build a Renderable, Sceneform loads its resources in the background while returning
                  // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
                  ModelRenderable.builder()
                          .setSource(HelloSceneformActivity.this, RawID)
                          .build()
                          .thenAccept(renderable -> HouseRenderable = renderable)
                          .exceptionally(
                                  throwable -> {
                                      Toast toast =
                                              Toast.makeText(HelloSceneformActivity.this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                                      toast.setGravity(Gravity.CENTER, 0, 0);
                                      toast.show();
                                      return null;
                                  });
                  return true;
              }
              else {
                  return false;
              }
          }
      });
        //主要AR銀幕呈現的部分在這以下
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (HouseRenderable == null) {
                        return;
                    }

                    if (plane.getType() != Type.HORIZONTAL_UPWARD_FACING) {
                        return;
                    }

                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(HouseRenderable);
                    andy.select();
                });

/*
      //-----------------------------------------------------------------------------------------------------------------------------------------------------------------------
      //主要AR銀幕呈現的部分在這以下
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        // When you build a Renderable, Sceneform loads its resources in the background while returning
        // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().
        ModelRenderable.builder()
                .setSource(this, RawID)
                .build()
                .thenAccept(renderable -> HouseRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
*/
  }
}
