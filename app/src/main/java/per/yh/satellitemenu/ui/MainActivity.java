package per.yh.satellitemenu.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import per.yh.satellitemenu.R;
import per.yh.satellitemenu.view.SatelliteMenu;


public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private List<String> mList = new ArrayList<String>();

    private SatelliteMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();
        initView();
    }

    private void initDate() {
        for(int i='A'; i<='z'; i++) {
            mList.add(""+(char)i);
        }
    }

    private void initView() {
        menu = (SatelliteMenu) findViewById(R.id.menu);
        mListView = (ListView) findViewById(R.id.listview);
        mListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList));
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d("Main", "click");
                if(menu.isOpen()) {
                    menu.toggleMenu(500);
                }
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
                if(menu.isOpen()) {
                    menu.toggleMenu(500);
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
            }
        });
    }


}
