package kr.pak.cup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnKeyListener
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.text.SimpleDateFormat
import java.util.Date

class TodoFragment : Fragment() {


    private lateinit var rootView: ViewGroup

    private var todoListItemSave = ArrayList<String>()

    private lateinit var addButton: Button
    private lateinit var inputTodo: EditText
    private lateinit var todoList: ListView

    private lateinit var todoListItems: ArrayList<String>

    private lateinit var adapter: ArrayAdapter<String>

    val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_todo, container, false) as ViewGroup

        addButton = rootView.findViewById(R.id.todoAddButton)
        inputTodo = rootView.findViewById(R.id.todoAddInput)
        todoList = rootView.findViewById(R.id.todoListView)

        todoListItems = readTodoList()

        adapter = activity?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
        }!!

        todoList.adapter = adapter

        addButton.setOnClickListener {
            if (inputTodo.text.equals("")) {
                Toast.makeText(context, "할 일을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else {
                var now = System.currentTimeMillis()
                var date = Date(now)
                todoListItems.add("${format.format(date)}  |  " + inputTodo.text)
                adapter = activity?.let {
                    ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
                }!!
                todoList.adapter = adapter
            }
        }

        todoList.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = (view as TextView).text.toString()
            adapter.remove(selectedItem)

            Toast.makeText(context, selectedItem + "(이)가 제거되었습니다", Toast.LENGTH_SHORT).show()
            todoList.adapter = adapter

            true
        }

        inputTodo.setOnKeyListener(object : OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (inputTodo.text.equals("")) {
                        Toast.makeText(context, "할 일을 입력하세요!", Toast.LENGTH_SHORT).show()
                    } else {
                        var now = System.currentTimeMillis()
                        var date = Date(now)
                        todoListItems.add("${format.format(date)}  |  " + inputTodo.text)
                        adapter = activity?.let {
                            ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
                        }!!
                        todoList.adapter = adapter
                    }
                    return true
                }
                return false
            }
        })

        return rootView
    }

    override fun onPause() {
        super.onPause()

        todoListItemSave = todoListItems

        saveTodoList(todoListItemSave)
    }

    override fun onResume() {
        super.onResume()

        todoListItems = readTodoList()
        adapter = activity?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
        }!!

        todoList.adapter = adapter
    }

    private fun saveTodoList(list: ArrayList<String>) {
        try {
            val fileOutputStream = activity?.openFileOutput("todoList.dat", Context.MODE_PRIVATE)
            val objectOutputStream = ObjectOutputStream(fileOutputStream)
            objectOutputStream.writeObject(list)
            objectOutputStream.close()
            fileOutputStream?.close()
            // Log.d("TodoFragment", "TodoList saved to internal storage")
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.d("todo_save", "TodoList saved")
    }

    private fun readTodoList(): ArrayList<String> {
        var list: ArrayList<String> = ArrayList<String>()
        try {
            val fileInputStream = activity?.openFileInput("todoList.dat")
            val objectInputStream = ObjectInputStream(fileInputStream)
            list = objectInputStream.readObject() as ArrayList<String>
            objectInputStream.close()
            fileInputStream?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        Log.d("todo_read", "TodoList Read")

        return list
    }

    override fun onDestroy() {
        super.onDestroy()
        saveTodoList(todoListItems)
    }

    override fun onDetach() {
        super.onDetach()
        saveTodoList(todoListItems)
    }
}