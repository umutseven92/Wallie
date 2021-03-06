package com.graviton.Cuzdan;

import Fragments.ExpenseDialogFragment;
import Helpers.Billing.IabHelper;
import Helpers.ExpenseAddListener;
import Helpers.IncomeAddListener;
import Helpers.RecordsHelper;
import Helpers.User;
import android.app.Application;


/**
 * Created by Umut Seven on 18.11.2014, for Graviton.
 */
public class Global extends Application {

    private User _user;

    public void SetUser(User user) {
        _user = user;
    }

    public User GetUser() {
        return _user;
    }

    private String _filePath;

    public void SetFilePath(String path) {
        _filePath = path;
    }

    public String GetFilePath() {
        return _filePath;
    }

    public ExpenseDialogFragment expenseDialog;

    public IncomeAddListener incomeAddListener;

    public ExpenseAddListener expenseAddListener;

    public IabHelper iabHelper;

    public RecordsHelper recordsHelper;

    private boolean _first;

    public void SetFirst(boolean first) {
        _first = first;
    }

    public boolean GetFirst() {
        return _first;
    }
}
