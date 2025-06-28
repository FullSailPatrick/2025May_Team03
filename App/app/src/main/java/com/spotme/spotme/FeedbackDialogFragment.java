package com.spotme.spotme;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FeedbackDialogFragment extends DialogFragment
{
    private RatingBar ratingBar;
    private EditText feedbackInput;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.feedback_modal, container, false);

        // Link views
        ImageButton closeButton = view.findViewById(R.id.closeButton);
        ratingBar = view.findViewById(R.id.ratingBar);
        feedbackInput = view.findViewById(R.id.feedbackInput);
        Button submitFeedbackButton = view.findViewById(R.id.submitFeedbackButton);

        // Close
        closeButton.setOnClickListener(v -> dismiss());

        // Submit
        submitFeedbackButton.setOnClickListener(v -> sendFeedback());

        return view;
    }

    // This function saves the feedback to the Firebase collection
    private void sendFeedback()
    {
        float rating = ratingBar.getRating();
        String feedback = feedbackInput.getText().toString().trim();

        //Validate empty fields
        if (feedback.isEmpty())
        {
            Toast.makeText(getContext(), "Please enter feedback.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Create a map
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("rating", rating);
        feedbackData.put("feedback", feedback);
        feedbackData.put("timestamp", new Timestamp(new Date()));

        //Instantiate the collection.
        FirebaseFirestore.getInstance()
                .collection("feedback")
                .add(feedbackData)
                .addOnSuccessListener(doc ->
                {
                    Toast.makeText(getContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                    dismiss();
                })
                .addOnFailureListener(e ->
                {
                    Toast.makeText(getContext(), "Failed to send feedback.", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public int getTheme()
    {
        return R.style.FullScreenDialogTheme;
    }
}
