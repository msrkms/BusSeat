package ViewModel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import Model.Seat;
import Repository.SeatRepository;

public class SeatViewModel extends AndroidViewModel {
    private Context context;
    private SeatRepository seatRepository;
    int NO_OF_COLUMN=0;
    int NO_OF_CORRIDOR=0;
    int CORRIDOR_LENGTH=0;
    private MutableLiveData<List<Seat>> listMutableLiveData;
    public SeatViewModel(@NonNull Application application) {
        super(application);
        this.context=application;
        seatRepository=new SeatRepository();
    }

    public LiveData<List<Seat>> getData(){
      String data=  seatRepository.getJsonFromAssets(context);
      listMutableLiveData=new MutableLiveData<>();
      List<Seat> seatList=new ArrayList<>();

        try {
            JSONArray jsonArray=new JSONArray(data);
            for (int i = 0; i < jsonArray.length(); i++) {
                if(checkArray(jsonArray.get(i).toString())){
                    JSONArray jsonArrayNested =jsonArray.getJSONArray(i);
                    if(NO_OF_COLUMN<jsonArrayNested.length()){
                        NO_OF_COLUMN=jsonArrayNested.length();
                    }

                    for (int j = 0; j < jsonArrayNested.length(); j++) {
                        JSONObject jsonObject=jsonArrayNested.getJSONObject(j);
                        Seat seat =new Seat();
                        seat.setSeatType(jsonObject.getString("t"));
                        if(seat.getSeatType().equals("s")){
                            seat.setSeatNo(jsonObject.getString("n"));
                        }else if(seat.getSeatType().equals("c")){
                            NO_OF_CORRIDOR=NO_OF_CORRIDOR+1;
                            CORRIDOR_LENGTH=Integer.valueOf(jsonObject.getString("r"));
                        }
                        seatList.add(seat);
                    }
                }else {
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    Iterator<String> keys=jsonObject.keys();
                    while (keys.hasNext()){
                        Seat seat=new Seat();
                        JSONObject objectNested=jsonObject.getJSONObject(keys.next());
                        seat.setSeatType(objectNested.getString("t"));
                        if(seat.getSeatType().equals("s")){
                            seat.setSeatNo(objectNested.getString("n"));
                        }
                        seatList.add(seat);

                    }
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("error"+e.toString());
        }

        List<Seat> seatListFinal=  makeSeatFillBuss(seatList,NO_OF_CORRIDOR,NO_OF_COLUMN);

        listMutableLiveData.setValue(seatListFinal);
        return listMutableLiveData;
    }

    private List<Seat> makeSeatFillBuss(List<Seat> seatList,int NO_OF_CORRIDOR,int NO_OF_COLUMN){

        List<Seat> seatListFinal=new ArrayList<>();
        int totalSeat=0;
        if(NO_OF_CORRIDOR>0){
            totalSeat= seatList.size()+(CORRIDOR_LENGTH*NO_OF_CORRIDOR)-1;
        }else {
            totalSeat=seatList.size();
        }

        int noOfRow=totalSeat/NO_OF_COLUMN;
        int[] IndexOFCorridor= new  int[NO_OF_CORRIDOR];
        int found=0;
        for (int i = 0; i < seatList.size(); i++) {
            if(seatList.get(i).getSeatType().equals("c")){
                IndexOFCorridor[found]=i;
                seatList.remove(i);
                found=found+1;
            }
        }



        int haveToSkipIndex=0;
        int[] haveToSKIP=new int[NO_OF_CORRIDOR*CORRIDOR_LENGTH];
        int rowStartIndex=0;
        for (int i = 0; i < noOfRow; i++) {
            if(i<CORRIDOR_LENGTH){
                for (int j = 0; j < NO_OF_CORRIDOR; j++) {
                    haveToSKIP[haveToSkipIndex]=rowStartIndex+IndexOFCorridor[j];
                    haveToSkipIndex=haveToSkipIndex+1;
                }
            }else{
            }
            rowStartIndex=rowStartIndex+NO_OF_COLUMN;
        }
        int oldIndex=0;
        int s=0;
        for (int i = 0; i <totalSeat; i++) {
            boolean skip=false;
            for (int j = 0; j < haveToSKIP.length; j++) {
                if(i==haveToSKIP[j]){
                    Seat seat =new Seat();
                    seat.setSeatType("c");
                    seatListFinal.add(seat);
                    s++;
                    skip=true;
                }
            }
            if(!skip) {
                seatListFinal.add(seatList.get(oldIndex));
                oldIndex=oldIndex+1;

            }

        }


        return seatListFinal;
    }


    public int getNO_OF_COLUMN() {
        return NO_OF_COLUMN;
    }

    private Boolean checkArray(String s){
        try {
            JSONArray jsonArray=new JSONArray(s);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

    }

}
