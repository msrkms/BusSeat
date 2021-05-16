package View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.busseat.R;

import java.util.List;

import Model.Seat;
import ViewModel.SeatViewModel;
import adapter.RecyclerViewAdapterSeat;

public class MainActivity extends AppCompatActivity {
    private SeatViewModel seatViewModel;
    private RecyclerView recyclerViewSeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerViewSeat=findViewById(R.id.recyclerViewSeat);
        seatViewModel=new ViewModelProvider(this,new ViewModelProvider.AndroidViewModelFactory(getApplication())).get(SeatViewModel.class);
        seatViewModel.getData().observe(MainActivity.this, new Observer<List<Seat>>() {
            @Override
            public void onChanged(List<Seat> seats) {

                RecyclerViewAdapterSeat recyclerViewAdapterSeat=new RecyclerViewAdapterSeat(seats);
                recyclerViewSeat.setAdapter(recyclerViewAdapterSeat);
                recyclerViewSeat.setLayoutManager(new GridLayoutManager(getApplicationContext(),seatViewModel.getNO_OF_COLUMN()));
            }
        });


    }
}