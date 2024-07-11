package com.example.sqlite_crud_operation

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var marksEditText: EditText
    private lateinit var addButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var studentAdapter: StudentAdapter
    private var studentList = mutableListOf<Student>()
    private var currentStudentId: Int? = null

    private lateinit var dbHelper: Helper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = Helper(this)

        nameEditText = findViewById(R.id.editText_name)
        surnameEditText = findViewById(R.id.editText_surname)
        marksEditText = findViewById(R.id.editText_Marks)
        addButton = findViewById(R.id.button_add)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        studentAdapter = StudentAdapter(studentList, { student -> editStudent(student) }, { student -> showDeleteConfirmationDialog(student) })
        recyclerView.adapter = studentAdapter
        fetchAllData()

        addButton.setOnClickListener {
            if (currentStudentId == null) {
                addData()
            } else {
                updateData()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteStudent(student: Student) {
        val success = dbHelper.deleteData(student)
        if (success) {
            showToast("Student deleted successfully")
            fetchAllData()
        } else {
            showToast("Failed to delete student")
        }

    }

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun editStudent(student: Student) {
        currentStudentId = student.id
        nameEditText.setText(student.name)
        surnameEditText.setText(student.surname)
        marksEditText.setText(student.marks.toString())
        addButton.text = "Update"
    }

    private fun addData() {
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val marksStr = marksEditText.text.toString()

        if (name.isEmpty() || surname.isEmpty() || marksStr.isEmpty()) {
            nameEditText.error = "Required"
            surnameEditText.error = "Required"
            marksEditText.error = "Required"
            showToast("Please fill in all fields")
            return
        }

        val marks = marksStr.toIntOrNull()
        if (marks == null) {
            showToast("Invalid marks value")
            return
        }

        val success = dbHelper.insertData(name, surname, marks)
        if (success) {
            showToast("Data added successfully")
            fetchAllData()
            clearFields()
        } else {
            showToast("Failed to add data")
        }
    }

    private fun updateData() {
        val id = currentStudentId ?: return
        val name = nameEditText.text.toString()
        val surname = surnameEditText.text.toString()
        val marksStr = marksEditText.text.toString()

        if (name.isEmpty() || surname.isEmpty() || marksStr.isEmpty()) {
            nameEditText.error = "Required"
            surnameEditText.error = "Required"
            marksEditText.error = "Required"
            showToast("Please fill in all fields")
            return
        }

        val marks = marksStr.toIntOrNull()
        if (marks == null) {
            showToast("Invalid marks value")
            return
        }

        val success = dbHelper.updateData(id, name, surname, marks)
        if (success) {
            showToast("Data updated successfully")
            fetchAllData()
            clearFields()
            currentStudentId = null
            addButton.text = "Add"
        } else {
            showToast("Failed to update data")
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchAllData() {
        val dataList = dbHelper.getAllData()
        studentList.clear()
        studentList.addAll(dataList)
        studentAdapter.notifyDataSetChanged()
    }

    private fun clearFields() {
        nameEditText.text.clear()
        surnameEditText.text.clear()
        marksEditText.text.clear()
        nameEditText.clearFocus()
        surnameEditText.clearFocus()
        marksEditText.clearFocus()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    private fun showDeleteConfirmationDialog(student: Student){
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Are you sure you want to delete this Student Record?")
            .setPositiveButton("Yes") {dialog, id ->
                deleteStudent(student)
            }
            .setNegativeButton("No") {dialog, id ->
                dialog.dismiss()
            }
        builder.create().show()
    }


    override fun onDestroy() {
        super.onDestroy()
        dbHelper.close()
    }
}