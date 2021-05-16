package Repository;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class SeatRepository {

   public String getJsonFromAssets(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open("data1.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch ( IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }

}
