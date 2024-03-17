package kr.pak.cup

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow

class GroupFragment : Fragment() {

    private lateinit var rootView: ViewGroup

    private lateinit var tableLayout: TableLayout
    private lateinit var tableRow: TableRow

    private lateinit var addButton: Button

    private lateinit var alert: AlertDialog.Builder
    private lateinit var groupCodeInput: EditText

    private var groupCode: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootView = inflater.inflate(R.layout.fragment_group, container, false) as ViewGroup

        addButton = rootView.findViewById(R.id.groupAddButton)

        alert = AlertDialog.Builder(context)
        groupCodeInput = EditText(context)

        alert.setTitle("그룹 코드")
        alert.setMessage("새로운 코드를 입력하시면, 새로운 그룹이 생성됩니다")
        alert.setView(groupCodeInput)
        alert.setPositiveButton("확인", DialogInterface.OnClickListener { _, _ ->
            groupCode = groupCodeInput.text.toString()
            Log.d("GroupCode", groupCode)
        })

        alert.setNegativeButton("취소", DialogInterface.OnClickListener { _, _ ->
            groupCodeInput.setText("")
        })

        val alertDialog = alert.create()

        tableLayout = rootView.findViewById(R.id.groupTable)
        tableRow = TableRow(context)
        tableRow.layoutParams = TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT)

        addButton.setOnClickListener {
            alertDialog.show()
        }

        return rootView
    }


}