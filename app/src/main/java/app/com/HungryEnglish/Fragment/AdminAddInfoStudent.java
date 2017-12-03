package app.com.HungryEnglish.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import app.com.HungryEnglish.Interface.AdminAddInfoStudentView;
import app.com.HungryEnglish.Presenter.AdminAddInfoStudentPresenter;
import app.com.HungryEnglish.R;
import app.com.HungryEnglish.Util.Utils;
import app.com.HungryEnglish.databinding.DialogAddLinkBinding;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link AdminAddInfoStudent#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminAddInfoStudent extends Fragment implements View.OnClickListener, AdminAddInfoStudentView {

    private DialogAddLinkBinding dialogBinding;
    private Dialog dialog;
    private AdminAddInfoStudentPresenter presenter;

    public AdminAddInfoStudent() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AdminAddInfoStudent newInstance() {
        AdminAddInfoStudent fragment = new AdminAddInfoStudent();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        presenter = new AdminAddInfoStudentPresenter(getActivity().getApplicationContext());
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_admin_add_info_student, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_student, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_link:
                dialog = new Dialog(getActivity());
                dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.dialog_add_link, null, false);
                dialog.setContentView(dialogBinding.getRoot());
                dialogBinding.submitLink.setOnClickListener(this);
                dialog.show();
                break;
        }
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_link:
                if (dialogBinding != null && dialog != null) {
                    presenter.addLink(String.valueOf(dialogBinding.edtLink.getText()), String.valueOf(dialogBinding.edtLink.getText()));
                }
                break;
        }
    }

    @Override
    public void showProgressDialog() {
        Utils.showDialog(getActivity());
    }

    @Override
    public void hideProgressDialog() {
        Utils.dismissDialog();
    }

    @Override
    public void showErrorMsg(String msg) {
        Toast.makeText(getActivity().getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getErrorInAddText(String message) {
        if (dialog != null) {
            Toast.makeText(getActivity().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
