package vn.vnpt.ansv.bts.ui.monitor;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Timestamp;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/9/2017.
 */

public class RecyclerMonitorAdapter extends RecyclerView.Adapter<RecyclerMonitorAdapter.DeviceHolder> {

    private List<MinSensorFullObj> dataSet;
    private OnDeviceItemClickListener listener;

    private static final int TYPE_TEMP_OUTSIDE = 0;
    private static final int TYPE_HUMI_INSIDE = 1;
    private static final int TYPE_CELL_INSIDE = 2;

    public RecyclerMonitorAdapter(List<MinSensorFullObj> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CELL_INSIDE;
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View viewCell = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_device, parent, false);
        return new DeviceHolder(viewCell, TYPE_CELL_INSIDE);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {

        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        holder.cardView.setLayoutParams(lp);
        holder.cardView.requestLayout();
        //
        String measurement = dataSet.get(position).getSensorInfo().getMeasurementUnit();
        Timestamp updateTime = dataSet.get(position).getSensorData().getList().get(0).getCreatedTime();
        double value = dataSet.get(position).getSensorData().getList().get(0).getValue();
        int batteryValue = dataSet.get(position).getSensorData().getList().get(0).getBattery();
        int statusId = dataSet.get(position).getSensorData().getList().get(0).getStatusId();
        int sensorTypeId = dataSet.get(position).getSensorInfo().getSensorTypeId();
        int warningModeId = dataSet.get(position).getSensorInfo().getWarningModeId();
        int warningComp = dataSet.get(position).getSensorInfo().getWarningComp();
        int warningValue1 = dataSet.get(position).getSensorInfo().getWarningValue1();
        int warningValue2 = dataSet.get(position).getSensorInfo().getWarningValue2();
        holder.txtSensorName.setText(dataSet.get(position).getSensorInfo().getSensorName().toUpperCase());
        holder.txtSensorValue.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForSensorValue(statusId)));
        holder.imgSensorIcon.setImageResource(Utils.setSensorIconImageView(statusId, sensorTypeId));
        holder.txtSensorValue.setText(String.format("%.0f",value));
        holder.txtBatteryValues.setText(batteryValue + "%");
        holder.txtBatteryValues.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForBatteryValue(batteryValue)));
        holder.imgBatteryIcon.setImageResource(Utils.setBatteryImageView(batteryValue));
        holder.imgSensorIcon.setImageResource(Utils.setSensorIconImageView(statusId, sensorTypeId));
        holder.txtUpdateTime.setText(splitTimeStamp(updateTime.toString()));

        holder.txtUpdateTime.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForUpdateTime(statusId)));

        if (measurement == null) {
            holder.txtMeasurementUnit.setText("");
        } else {
            holder.txtMeasurementUnit.setText("(" + measurement + ")");
        }
        if (statusId == 2) {
            holder.txtMeasurementUnit.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), R.color.sl_footer_grey));
        } else {
            holder.txtMeasurementUnit.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), R.color.sl_grey));
        }

       /* if (warningModeId == 1 && warningComp > 0) {
            if ((warningComp == 1 && value <= warningValue1)
                    || (warningComp == 2 && value >= warningValue1)
                    || (warningComp == 3 && (value >= warningValue1 && value <= warningValue2))
                    || (warningComp == 4 && (value <= warningValue1 || value >= warningValue2))) {
                holder.txtSensorValue.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForSensorValue(statusId)));
                holder.imgSensorIcon.setImageResource(Utils.setSensorIconImageView(statusId, sensorTypeId));
            } else {
                holder.txtSensorValue.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForSensorValue(statusId)));
                holder.imgSensorIcon.setImageResource(Utils.setSensorIconImageView(statusId, sensorTypeId));
            }
        }*/

        splitTimeStamp(updateTime.toString()+"");

    }

    public String splitTimeStamp(String timeStampString) {
        String[] timeStamp = timeStampString.split(" ");
        String[] date = timeStamp[0].split("\\-");
        String[] fullTime = timeStamp[1].split("\\.");
        String time = fullTime[0];
        String yyyy = date[0];
        String MM = date[1];
        String dd = date[2];
        return time + ", " + dd + "/" + MM + "/" + yyyy;
    }

    public void updateDataSet(List<MinSensorFullObj> listSensorObj) {
        this.dataSet = null;
        this.dataSet = listSensorObj;
        notifyDataSetChanged();
    }

    public void setListener(OnDeviceItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnDeviceItemClickListener {
        void onDeviceItemClick(View view, Object device);
    }

    public static class DeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.txtSensorName)
        TextView txtSensorName;

        @BindView(R.id.txtSensorValue)
        TextView txtSensorValue;

        @BindView(R.id.txtMeasurementUnit)
        TextView txtMeasurementUnit;

        @BindView(R.id.txtBatteryValues)
        TextView txtBatteryValues;

        @BindView(R.id.imgSensorIcon)
        ImageView imgSensorIcon;

        @BindView(R.id.imgBatteryIcon)
        ImageView imgBatteryIcon;

        @BindView(R.id.txtUpdateTime)
        TextView txtUpdateTime;

        View rootView;

        public DeviceHolder(View view, int viewType) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
        }
    }
}
