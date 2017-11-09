package vn.vnpt.ansv.bts.objects;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ANSV on 11/9/2017.
 */

public class StatusResponse {
    public void checkResponse(int statusCode,Context context){
        switch (statusCode){
            case 1:
                Toast.makeText(context,"Thành công!", Toast.LENGTH_LONG).show();
                break;
            case 2:
                Toast.makeText(context,"Đối tượng này đã tồn tại!", Toast.LENGTH_LONG).show();
                break;
            case 3:
                Toast.makeText(context,"Đối tượng này không tồn tại!", Toast.LENGTH_LONG).show();
                break;
            case 4:
                Toast.makeText(context,"Không tìm thấy dữ liệu!", Toast.LENGTH_LONG).show();
                break;
            case 5:
                Toast.makeText(context,"Không đủ quyền thực hiện!", Toast.LENGTH_LONG).show();
                break;
            case 6:
                Toast.makeText(context,"Sai thông số!", Toast.LENGTH_LONG).show();
                break;
            case 7:
                Toast.makeText(context,"API không hợp lê!", Toast.LENGTH_LONG).show();
                break;
            case 8:
                Toast.makeText(context,"Key API not found!", Toast.LENGTH_LONG).show();
                break;
            case 9:
                Toast.makeText(context,"Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_LONG).show();
                break;
            case 10:
                break;
        }
    }
}
