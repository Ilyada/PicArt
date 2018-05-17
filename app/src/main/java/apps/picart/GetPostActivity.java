package apps.picart;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import apps.picart.Objects.Posts;

public class GetPostActivity extends AppCompatActivity {
TextView tv_name,tv_mail,tv_desc,tv_title;
ImageView img_pp,img_post;
ImageButton add_fav;
boolean add;
int postindex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getpost);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //tv=findViewById(R.id.tvAnimalNameS);

        final Posts targetpost = (Posts) getIntent().getSerializableExtra("MyClass");

        tv_name=findViewById(R.id.tv_gonderennick);
        tv_mail=findViewById(R.id.tv_gonderenmail);
        tv_desc=findViewById(R.id.tv_getpostdesc);
        tv_title=findViewById(R.id.tv_getpostitle);
        img_pp = findViewById(R.id.img_getpostgonderenpp);
        img_post =findViewById(R.id.img_getpostimg);
        add_fav =findViewById(R.id.add_fav);



        add=true;

// CONTROL THE POST ( IS IT FAV. OR NOT )
        for(int i=0;i<MainActivity.currentuser.myfavposts.size();i++){
            if(MainActivity.currentuser.myfavposts.get(i).imageUrl.equalsIgnoreCase(targetpost.imageUrl)
                    && MainActivity.currentuser.myfavposts.get(i).sendername.equalsIgnoreCase(targetpost.sendername)
                    && MainActivity.currentuser.myfavposts.get(i).title.equalsIgnoreCase(targetpost.title)
                    && MainActivity.currentuser.myfavposts.get(i).desc.equalsIgnoreCase(targetpost.desc)
                    && MainActivity.currentuser.myfavposts.get(i).sendermail.equalsIgnoreCase(targetpost.sendermail)){

                postindex=i;
                add=false;
                add_fav.setImageResource(R.drawable.ic_addfav);

            }
        }

        tv_desc.setText(targetpost.desc);
        tv_title.setText(targetpost.title);
        tv_name.setText(" "+targetpost.sendername);
        tv_mail.setText("  "+targetpost.sendermail);

        Picasso.with(this.getBaseContext()) //Context
                .load(targetpost.senderpp) //URL/FILE
                .into(img_pp);

        TextView mTitle = (TextView) findViewById(R.id.tv_getposttitle);
        Typeface typeface = Typeface.createFromAsset(this.getAssets(),"Billabong.ttf");
        mTitle.setTypeface(typeface);
        ImageButton btn_back = (ImageButton) findViewById(R.id.btn_getpostback);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetPostActivity.super.onBackPressed();
            }
        });

        Picasso.with(this.getBaseContext()) //Context
                .load(targetpost.getImageUrl()) //URL/FILE
                .into(img_post);




        add_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(add){
                    add_fav.setImageResource(R.drawable.ic_addfav);
                    MainActivity.currentuser.myfavposts.add(targetpost);
                    System.out.println("FAVORİ POST SAYISI:"+MainActivity.currentuser.myfavposts.size());
                    add=false;
                    favpost_db_process(targetpost, true);
                }
                else{
                    MainActivity.currentuser.myfavposts.remove(postindex);

                    System.out.println("FAVORİ POST SAYISI:"+MainActivity.currentuser.myfavposts.size());
                    add_fav.setImageResource(R.drawable.ic_nofav);
                    add=true;
                    favpost_db_process(targetpost, false);

                }
                YoYo.with(Techniques.Tada)
                        .duration(700)
                        .repeat(1)
                        .playOn(add_fav);
            }
        });


        //tv.setText(targetpost);
    }

    private void favpost_db_process(final Posts targetpost, Boolean add){

        if(add) {
            DatabaseReference oku_userfavposts = MainActivity.db.getReference()
                    .child("/Users")
                    .child(MainActivity.userid)
                    .child("/MyFavPosts");
            String key = oku_userfavposts.push().getKey();
            DatabaseReference dbRefKeyli = MainActivity.db.getReference().
                    child("Users").
                    child(MainActivity.auth.getCurrentUser().getUid()).
                    child("MyFavPosts/")
                    .child(key);


            dbRefKeyli.setValue(targetpost);
        }
        else{


                DatabaseReference oku_Userposts = MainActivity.db.getReference()
                        .child("/Users")
                        .child(MainActivity.userid)
                        .child("/MyFavPosts");

                oku_Userposts.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Iterable<DataSnapshot> keys = dataSnapshot.getChildren();
                        for(DataSnapshot key : keys){
                            Posts post = key.getValue(Posts.class);
                            if(post.imageUrl.equalsIgnoreCase(targetpost.imageUrl)
                                    && post.sendername.equalsIgnoreCase(targetpost.sendername)
                                    && post.title.equalsIgnoreCase(targetpost.title)
                                    && post.desc.equalsIgnoreCase(targetpost.desc)
                                    && post.sendermail.equalsIgnoreCase(targetpost.sendermail)){

                                DatabaseReference dbrefkeyli = MainActivity.db.getReference()
                                        .child("/Users")
                                        .child(MainActivity.userid)
                                        .child("/MyFavPosts")
                                        .child(key.getKey());
                                dbrefkeyli.removeValue();

                            }


                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        }


    }

}
