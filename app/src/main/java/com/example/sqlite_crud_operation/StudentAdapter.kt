package com.example.sqlite_crud_operation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(private val studentList: List<Student>, private val onEditClick: (Student) -> Unit,
    private val onDeleteClick: (Student) -> Unit ) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val surnameTextView: TextView = itemView.findViewById(R.id.surnameTextView)
        val marksTextView: TextView = itemView.findViewById(R.id.marksTextView)
        val editButton: Button = itemView.findViewById(R.id.button_edit)
        val deleteButton: Button = itemView.findViewById(R.id.button_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val currentStudent = studentList[position]
        holder.nameTextView.text = currentStudent.name
        holder.surnameTextView.text = currentStudent.surname
        holder.marksTextView.text = currentStudent.marks.toString()

        holder.editButton.setOnClickListener {
            onEditClick(currentStudent)
        }

        holder.deleteButton.setOnClickListener {
            onDeleteClick(currentStudent)
        }
    }

    override fun getItemCount(): Int {
        return studentList.size
    }
}