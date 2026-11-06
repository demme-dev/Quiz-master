package com.example.quizeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.quizeapp.R
import com.example.quizeapp.databinding.ItemReviewQuestionBinding
import com.example.quizeapp.model.UserAnswerRecord

/**
 * RecyclerView Adapter for displaying detailed question review after completing a quiz.
 */
class ReviewAdapter(
    private val records: List<UserAnswerRecord>
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(val binding: ItemReviewQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val binding = ItemReviewQuestionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReviewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val record = records[position]
        val context = holder.itemView.context
        val b = holder.binding

        b.tvQuestionNumBadge.text = "Q${position + 1}"
        b.tvReviewQuestionText.text = record.questionText

        val greenColor = ContextCompat.getColor(context, R.color.correct_green)
        val redColor = ContextCompat.getColor(context, R.color.wrong_red)

        if (record.isCorrect) {
            b.tvStatusIcon.text = "✔"
            b.tvStatusIcon.setTextColor(greenColor)
        } else {
            b.tvStatusIcon.text = "✘"
            b.tvStatusIcon.setTextColor(redColor)
        }

        val userAnsText = if (record.userSelectedIndex != null && record.userSelectedIndex in record.options.indices) {
            record.options[record.userSelectedIndex]
        } else {
            "(Timed Out)"
        }

        b.tvReviewUserAnswer.text = context.getString(R.string.your_answer_format, userAnsText)
        b.tvReviewUserAnswer.setTextColor(if (record.isCorrect) greenColor else redColor)

        val correctAnsText = if (record.correctIndex in record.options.indices) {
            record.options[record.correctIndex]
        } else {
            ""
        }
        b.tvReviewCorrectAnswer.text = context.getString(R.string.correct_answer_format, correctAnsText)
        b.tvReviewCorrectAnswer.setTextColor(greenColor)
    }

    override fun getItemCount(): Int = records.size
}
