package kr.pak.cup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast

class TodoFragment : Fragment() {

    private lateinit var rootView: ViewGroup

    private var todoListItemSave = ArrayList<String>()

    private lateinit var addButton: Button
    private lateinit var inputTodo: EditText
    private lateinit var todoList: ListView

    private lateinit var todoListItems: ArrayList<String>

    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_todo, container, false) as ViewGroup

        addButton = rootView.findViewById(R.id.todoAddButton)
        inputTodo = rootView.findViewById(R.id.todoAddInput)
        todoList = rootView.findViewById(R.id.todoListView)

        todoListItems = ArrayList<String>()

        adapter = activity?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
        }!!

        addButton.setOnClickListener {
            if (inputTodo.text.equals("")) {
                Toast.makeText(context, "할 일을 입력하세요!", Toast.LENGTH_SHORT).show()
            } else {
                todoListItems.add((todoListItems.size + 1).toString() + ". " + inputTodo.text)
                adapter = activity?.let {
                    ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
                }!!
                todoList.adapter = adapter
            }

            /*for (i in 1..adapter.count) {
                todoListItems.add(adapter.getItem(i).toString())
                todoListItems.distinct()
            }*/ // 파일 저장
        }

        todoList.setOnItemLongClickListener { parent, view, position, id ->
            val selectedItem = (view as TextView).text.toString()
            adapter.remove(selectedItem)

            Toast.makeText(context, selectedItem + "(이)가 제거되었습니다", Toast.LENGTH_SHORT).show()
            todoList.adapter = adapter

            true
        }

        return rootView
    }

    override fun onPause() {
        super.onPause()

        todoListItemSave = todoListItems
    }

    override fun onResume() {
        super.onResume()

        todoListItems = todoListItemSave
        adapter = activity?.let {
            ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, todoListItems)
        }!!
        todoList.adapter = adapter
    }
}