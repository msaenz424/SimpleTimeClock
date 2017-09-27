package com.android.mig.simpletimeclock.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.mig.simpletimeclock.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;

import static android.app.Activity.RESULT_OK;

public class AddEmployeeDialogFragment extends DialogFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 10;        // code should be bigger than 0
    private NoticeDialogListener mNoticeDialogListener;
    private PhotoPickerListener mPhotoPickerListener;

    private ImageView mBlankImageView;
    private EditText mNameEditText;
    private EditText mWageEditText;
    private Uri mPhotoPath, mTempContainerPath;
    private PickImageDialog mPickImageDialog;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) context;
            mPhotoPickerListener = (PhotoPickerListener) context;
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
            mPhotoPickerListener = (PhotoPickerListener) activity;
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
                        .setSystemDialog(false);

                mPickImageDialog = PickImageDialog.build(pickSetup)
                        .setOnClick(new IPickClick() {
                            @Override
                            public void onGalleryClick() {
                            }

                            @Override
                            public void onCameraClick() {
                                tryUsingCamera();
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
                    if (!name.isEmpty() && !wage.isEmpty()) {
                        preparePhotoAndSave();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // receives result code from camera intent launched previously
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            mPhotoPath = mTempContainerPath;
            Glide.with(mBlankImageView.getContext()).load(mPhotoPath).apply(RequestOptions.circleCropTransform()).into(mBlankImageView);
            mTempContainerPath = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // if permission is granted at run time, then launch camera
                    dispatchCameraIntent();
                }
            }
        }
    }

    /**
     * checks for permission to use camera. Result is passed to onRequestPermissionsResult
     */
    private void tryUsingCamera() {
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int writePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (cameraPermissionCheck == PackageManager.PERMISSION_DENIED || writePermissionCheck == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_USE_CAMERA);
        } else if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            dispatchCameraIntent();
        }
        mPickImageDialog.dismiss();
    }

    /**
     * Launches camera intent with a temporary path
     */
    public void dispatchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "");
            mTempContainerPath = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTempContainerPath);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * Uploads the photo (if taken) to the storage and passes its link or null to saveGeoDiary
     */
    private void preparePhotoAndSave(){
        if (mPhotoPath != null){
            mPhotoPickerListener.onPhotoTaken(String.valueOf(mPhotoPath));
        }
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    public interface PhotoPickerListener {
        void onPhotoTaken(String photoPath);
    }
}
