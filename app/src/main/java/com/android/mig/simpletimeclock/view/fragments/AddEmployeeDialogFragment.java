package com.android.mig.simpletimeclock.view.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.activities.AllEmployeesActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

public class AddEmployeeDialogFragment extends DialogFragment{

    private NoticeDialogListener mNoticeDialogListener;

    private ImageView mBlankImageView;
    private EditText mNameEditText;
    private EditText mWageEditText;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    /**
     * Necessary deprecated method for older APIs
     *
     * @param activity host activity
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.add_employee_dialog, null);
        mBlankImageView = rootView.findViewById(R.id.blank_image_view);
        mNameEditText = rootView.findViewById(R.id.name_edit_text);
        mWageEditText = rootView.findViewById(R.id.wage_edit_text);

        mBlankImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PickSetup pickSetup = new PickSetup()
                        .setTitle(getResources().getString(R.string.dialog_add_photo_title))
                        .setSystemDialog(true);

                PickImageDialog.build(pickSetup)
                        .setOnPickResult(new IPickResult() {
                            @Override
                            public void onPickResult(PickResult r) {
                                if (r.getError() == null){
                                    Glide.with(getActivity().getApplicationContext())
                                            .load(r.getUri())
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(mBlankImageView);
                                    AllEmployeesActivity.sPhotoUri = r.getPath();
                                }
                            }
                        }).show((FragmentActivity) getActivity());
            }
        });

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because it's going in the dialog layout
        Glide.with(getActivity().getApplicationContext())
                .load(R.drawable.im_blank_profile)
                .apply(RequestOptions.circleCropTransform())
                .into(mBlankImageView);

        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.dialog_accept_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // see action on onResume method which prevents
                        // closing the dialog if EditText fields are empty
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = mNameEditText.getText().toString().trim();
                    String wage = mWageEditText.getText().toString().trim();
                    if (!name.isEmpty() && !wage.isEmpty()){
                        mNoticeDialogListener.onDialogPositiveClick(AddEmployeeDialogFragment.this);
                        alertDialog.dismiss();
                    }
                }
            });

            Button negativeButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
        }
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }
}
