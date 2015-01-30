package Fragments;

import Helpers.Income;
import Helpers.IncomeLoadListener;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import com.google.gson.Gson;
import com.graviton.Cuzdan.Global;
import com.graviton.Cuzdan.MainActivity;
import com.graviton.Cuzdan.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;


/**
 * Created by Umut Seven on 17.1.2015, for Graviton.
 */
public class IncomeDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final Income income = new Gson().fromJson(bundle.getString("income"), Income.class);

        builder.setTitle( Html.fromHtml("Detaylar"))
                .setMessage(String.format("Kategori: %s\n\nAlt Kategori: %s\n\nAçıklama: %s",income.GetCategory(),income.GetSubCategory(),income.GetDescription()))
                .setPositiveButton("Sil", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            DeleteIncome(income.GetID());
                            _listener.onDismissed();
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

        return builder.create();
    }

    private IncomeLoadListener _listener;

    public void SetListener(IncomeLoadListener listener)
    {
        _listener = listener;
    }

    public void DeleteIncome(String id) throws IOException {
        StringBuffer datax = new StringBuffer("");

        String filePath = ((Global) getActivity().getApplication()).GetFilePath();

        try
        {
            FileInputStream fIn = getActivity().openFileInput(filePath);
            InputStreamReader isr = new InputStreamReader(fIn);
            BufferedReader buffreader = new BufferedReader(isr) ;

            String readString = buffreader.readLine();
            while (readString != null) {
                datax.append(readString);
                readString = buffreader.readLine() ;
            }

            isr.close();
        }

        catch (IOException ioe)
        {
            ioe.printStackTrace();
        }

        String main = datax.toString();
        try
        {
            JSONObject mainJSON = new JSONObject(main);
            JSONObject userJSON = mainJSON.getJSONObject("user");
            JSONArray incomes = userJSON.getJSONArray("incomes");
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
                    "\t\t\"incomes\": [],\n" +
                    "\t\t\"expenses\": []\n" +
                    "\t}\n" +
                    "}\n",userJSON.getString("userName"),userJSON.getString("name"),userJSON.getString("lastName") );

            JSONObject userInfo = new JSONObject(userSettings);
            JSONArray newIncomes = userInfo.getJSONObject("user").getJSONArray("incomes");
            JSONArray newExpenses = userInfo.getJSONObject("user").getJSONArray("expenses");

            for(int i = 0; i< incomes.length();i++)
            {
                if(!incomes.getJSONObject(i).getString("id").equals(id))
                {
                    newIncomes.put(incomes.getJSONObject(i));
                }
            }

            for (int i = 0; i<expenses.length();i++)
            {
                newExpenses.put(expenses.getJSONObject(i));
            }

            FileOutputStream fileOutputStream = getActivity().openFileOutput(filePath, Context.MODE_PRIVATE);
            fileOutputStream.write(userInfo.toString().getBytes());
            fileOutputStream.close();
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
