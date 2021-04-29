package com.example.practicerecyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_DRAG;

public class MainActivity extends AppCompatActivity {

    private final List<String> dataset = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager rLayoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(rLayoutManager);

        int i = 0;
        while (i < 20) {
            String str = "Data_" + i;
            dataset.add(str);
            i++;
        }

        MyAdapter adapter = new MyAdapter(dataset);
        recyclerView.setAdapter(adapter);

        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        final Drawable deleteIcon = ContextCompat.getDrawable(this, R.mipmap.ic_delte_launcher);

        ItemTouchHelper mIth  = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN ,
                        ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        adapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Log.d("test",""+direction);
                        if (direction == ItemTouchHelper.LEFT) {
                            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        } else if (direction == ItemTouchHelper.RIGHT) {
//                            adapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                        }
                    }

                    @Override
                    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);
                        if (actionState == ACTION_STATE_DRAG) {
                            viewHolder.itemView.setAlpha(0.5f);
                        }
                    }

                    @Override
                    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                        super.clearView(recyclerView, viewHolder);
                        viewHolder.itemView.setAlpha(1.0f);
                    }

                    @Override
                    public void onChildDraw(@NonNull Canvas c,
                                            @NonNull RecyclerView recyclerView,
                                            @NonNull RecyclerView.ViewHolder viewHolder,
                                            float dX, float dY, int actionState,
                                            boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                        View itemView = viewHolder.itemView;

                        if (dX == 0f && !isCurrentlyActive) {
                            clearCanvas(c, itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, false);
                            return;
                        }

                        if (dX < 0) {
                            ColorDrawable background = new ColorDrawable();
                            background.setColor(Color.parseColor("#f44336"));
                            background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                            background.draw(c);

                            int deleteIconTop = itemView.getTop() + (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                            int deleteIconMargin = (itemView.getHeight() - deleteIcon.getIntrinsicHeight()) / 2;
                            int deleteIconLeft = itemView.getRight() - deleteIconMargin - deleteIcon.getIntrinsicWidth();
                            int deleteIconRight = itemView.getRight() - deleteIconMargin;
                            int deleteIconBottom = deleteIconTop + deleteIcon.getIntrinsicHeight();

                            deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom);
                            deleteIcon.draw(c);
                        } else {
                            // 右スワイプ
                        }
                    }
                });

        mIth.attachToRecyclerView(recyclerView);
    }

    private void clearCanvas(Canvas c, int left, int top, int right, int bottom) {
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        c.drawRect(left, top, right, bottom, paint);
    }

}