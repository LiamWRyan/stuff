package com.ashleytharp.uiwithfragmentspractice;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends FragmentActivity implements IOnHeadlineSelectedListener  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.fragment_container) != null){                  //if the View called fragment_container is defined...

            if(savedInstanceState != null){                                 //if savedInstanceState != null, that means we are being restored from a previous state
                return;                                                     //in that case we bail so we don't create overlapping identical fragments
            }

            HeadlinesFragment firstFragment = new HeadlinesFragment();      //this is going to have some news article headlines

            firstFragment.setArguments(getIntent().getExtras());            //if this fragment was started with special instructions from an Intent, we grab those extras

            //FragmentManager manages Fragment lifecycle mostly
            getSupportFragmentManager().beginTransaction()                  //Fragment transactions are essentially for adding, removing, and animating auch fragment transitions
                    .add(R.id.fragment_container, firstFragment, "")            //add HeadlinesFragment we just made to the root FragmentActivity
                    .commit();                                              //schedules this transaction to be executed on the main thread as soon as it becomes available again
                                                                            //note: this HeadlinesFragment was added at runtime, which means we can mess with it, it is not carved in stone

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onArticleSelected(int position) {
        // The user selected the headline of an article from the HeadlinesFragment so we're going to display the selected article

        ArticleFragment articleFragment = (ArticleFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_article);

        if(articleFragment != null){
            //if article fragment is available, we're in two pane layout

            articleFragment.updateArticleView(position);
        }else{
            //otherwise we're in the one pane layout and must swap fragments
            ArticleFragment newArticleFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newArticleFragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            //replace whatever is in the fragment_container view with this fragment
            //and add the transaction to the back stack so the user can navigate back,
            transaction.replace(R.id.fragment_container, newArticleFragment);
            transaction.addToBackStack(null);

            //commit the transaction
            transaction.commit();
        }

    }
}
