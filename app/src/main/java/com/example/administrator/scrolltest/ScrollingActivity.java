package com.example.administrator.scrolltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ScrollingActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        recyclerView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new ViewHolder(getLayoutInflater().inflate(R.layout.item_simple, parent, false));
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ViewHolder vh = (ViewHolder) holder;
                vh.text.setText("Fake Item " + (position + 1));
                vh.text2.setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit.");
            }

            @Override
            public int getItemCount() {
                return 20;
            }

            class ViewHolder extends RecyclerView.ViewHolder {

                TextView text;
                TextView text2;

                public ViewHolder(View itemView) {
                    super(itemView);

                    text = (TextView) itemView.findViewById(R.id.text);
                    text2 = (TextView) itemView.findViewById(R.id.text2);
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
       /* switch (v.getId()) {
            case R.id.search_tv:
                Toast.makeText(this, "点击事件按", Toast.LENGTH_SHORT).show();
                break;
            case R.id.edit_search:
                Toast.makeText(this, "点击事件按2", Toast.LENGTH_SHORT).show();
                break;
        }*/
    }
}
