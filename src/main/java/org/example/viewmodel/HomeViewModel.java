package org.example.viewmodel;

import gurux.dlms.enums.ObjectType;
import org.example.model.MeterField;
import org.example.service.MeterService;
import org.example.service.SecureStorageService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class HomeViewModel {
    private final SecureStorageService secureStorageService;
    private final MeterService meterService;

    private final ObservableList<MeterField> meterFields = FXCollections.observableArrayList();

    public HomeViewModel(SecureStorageService secureStorageService) {
        this.secureStorageService = secureStorageService;
        this.meterService = new MeterService();

        initializeMeterFields();
    }

    // =========================================================
    // EXISTING PASSWORD LOGIC
    // =========================================================

    public boolean isCurrentPassword(String password) {
        return secureStorageService.isCurrentPassword(password);
    }

    public void updatePassword(String newPassword) {
        secureStorageService.changePassword(newPassword);
    }

    // =========================================================
    // METER DATA
    // =========================================================

    private void initializeMeterFields() {
        meterFields.add(new MeterField("0.0.96.1.0.255", "Meter ID", "Current meter id", ObjectType.DATA));
        meterFields.add(new MeterField("1.1.0.2.0.255", "FW Version", "Current firmware version", ObjectType.DATA));
        meterFields.add(new MeterField("1.0.33.7.0.255", "Power Factor", "Current power factor", ObjectType.REGISTER));
        meterFields.add(new MeterField("1.0.129.129.12.255", "Recharge Number", "How many recharges the user made", ObjectType.REGISTER));
        meterFields.add(new MeterField("1.0.143.159.0.255", "Remaining Credit (kw)", "Remaining Credit by kw", ObjectType.REGISTER));
        meterFields.add(new MeterField("1.0.140.129.2.255", "Total Consumption (Money)", "Total Consumption by money", ObjectType.REGISTER));
    }

    public ObservableList<MeterField> getMeterFields() {
        return meterFields;
    }

    // =========================================================
    // METER CONNECTION
    // =========================================================

    public boolean connectMeter() {
        return meterService.connect();
    }

    public void disconnectMeter() {
        meterService.disconnect();
    }

    // =========================================================
    // READ SELECTED DATA
    // =========================================================

    public void getSelectedReadings() {
        List<MeterField> selectedFields = meterFields.stream()
                .filter(MeterField::isSelected)
                .collect(Collectors.toList());

        meterService.readFields(selectedFields);
    }
}