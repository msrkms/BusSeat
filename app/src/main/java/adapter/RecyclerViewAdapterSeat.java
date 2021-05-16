package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.busseat.R;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

import Model.Seat;

public class RecyclerViewAdapterSeat extends RecyclerView.Adapter<RecyclerViewAdapterSeat.ViewHolder>{
    private List<Seat> seatList;

    public RecyclerViewAdapterSeat(List<Seat> seatList) {
        this.seatList = seatList;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewAdapterSeat.ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.seat_item,parent,false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterSeat.ViewHolder holder, int position) {
        holder.setData(seatList.get(position));
    }

    @Override
    public int getItemCount() {
        if(seatList!=null){
            return seatList.size();
        }else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewSeat;
        MaterialCardView materialCardViewSeat;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSeat=itemView.findViewById(R.id.textViewSeatNO);
            materialCardViewSeat=itemView.findViewById(R.id.materialCardViewSeat);
        }

        public void setData(Seat data){

            if(data.getSeatType().equals("c")){
                materialCardViewSeat.setVisibility(View.GONE);
            }else if(data.getSeatType().equals("b")){
                textViewSeat.setText("");
            }else if(data.getSeatType().equals("s")){
                textViewSeat.setText(data.getSeatNo());
            }else{
                textViewSeat.setText(data.getSeatType());
            }
        }


    }
}
