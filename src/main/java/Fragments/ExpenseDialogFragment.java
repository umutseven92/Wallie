package Fragments;

import Helpers.DateFormatHelper;
import Helpers.Expense;
import Helpers.ExpenseLoadListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


/**
 * Created by Umut Seven on 17.1.2015, for Graviton.
 */
public class ExpenseDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final Expense expense = new Gson().fromJson(bundle.getString("expense"), Expense.class);
        boolean canDelete = bundle.getBoolean("canDelete");

        if (canDelete) {
            builder.setTitle(Html.fromHtml("Detaylar"))
                    .setMessage(String.format("Tarih: %s\n\nGider Türü: %s\n\nKategori: %s\n\nAlt Kategori: %s\n\nMiktar: %s\n\nAçıklama: %s", DateFormatHelper.GetDayText(expense.GetDate()), expense.GetTurkishStringTag(), expense.GetCategory(), expense.GetSubCategory(), expense.GetAmount().toString(), expense.GetDescription()))
                    .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                DeleteExpense(expense.GetID());
                                _listener.onDismissed();
                                if (_secondListener != null) {
                                    _secondListener.onDismissed();
                                }
                                dismiss();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).setNegativeButton("Geri", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dismiss();
                }
            });

        } else {
            builder.setTitle(Html.fromHtml("Detaylar"))
                    .setMessage(String.format("Tarih: %s\n\nGider Türü: %s\n\nKategori: %s\n\nAlt Kategori: %s\n\nMiktar: %s\n\nAçıklama: %s", DateFormatHelper.GetDayText(expense.GetDate()), expense.GetTurkishStringTag(), expense.GetCategory(), expense.GetSubCategory(), expense.GetAmount().toString(), expense.GetDescription()))
                    .setNegativeButton("Geri", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dismiss();
                        }
                    });
        }
        return builder.create();
    }

    private ExpenseLoadListener _listener;
    private ExpenseLoadListener _secondListener;

    public void SetListener(ExpenseLoadListener listener) {
        _listener = listener;
    }

    public void SetSecondListener(ExpenseLoadListener listener) {
        _secondListener = listener;
    }

    public void DeleteExpense(String id) throws IOException {
        StringBuffer datax = new StringBuffer("");

        String filePath = ((Global) getActivity().getApplication()).GetFilePath();

        try {
            FileInputStream fIn = getActivity().openFileInput(filePath);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr);

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine();
            }

            isr.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        String main = datax.toString();
        try {
            JSONObject mainJSON = new JSONObject(main);
            JSONObject userJSON = mainJSON.getJSONObject("user");
            JSONArray incomes = userJSON.getJSONArray("expenses");
            JSONArray expenses = userJSON.getJSONArray("expenses");

            String userSettings = String.format("{\n" +
                    "\t\"user\": {\n" +
                    "\t\t\"userName\": \"%s\",\n" +
                    "\t\t\"birthDate\": \"1992-08-05\",\n" +
                    "\t\t\"name\": \"%s\",\n" +
                    "\t\t\"lastName\": \"%s\",\n" +
                    "\t\t\"city\": \"Istanbul\",\n" +
                    "\t\t\"email\": \"umutseven92@gmail.com\",\n" +
                    "\n" +
                    "\t\t\"expenses\": [],\n" +
                    "\t\t\"expenses\": []\n" +
                    "\t}\n" +
                    "}\n", userJSON.getString("userName"), userJSON.getString("name"), userJSON.getString("lastName"));

            JSONObject userInfo = new JSONObject(userSettings);
            JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("expenses");
            JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");

            for (int i = 0; i < expenses.length(); i++) {
                if (!expenses.getJSONObject(i).getString("id").equals(id)) {
                    newExpenses.put(expenses.getJSONObject(i));
                }
            }

            for (int i = 0; i < incomes.length(); i++) {
                newIncomes.put(incomes.getJSONObject(i));
            }

            FileOutputStream fileOutputStream = getActivity().openFileOutput(filePath, Context.MODE_PRIVATE);
            fileOutputStream.write(userInfo.toString().getBytes());
            fileOutputStream.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
