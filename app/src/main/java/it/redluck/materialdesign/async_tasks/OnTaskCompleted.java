package it.redluck.materialdesign.async_tasks;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface OnTaskCompleted {

    void useInterfaceParameters(ArrayList<HashMap<Date, Double>> data);
}
