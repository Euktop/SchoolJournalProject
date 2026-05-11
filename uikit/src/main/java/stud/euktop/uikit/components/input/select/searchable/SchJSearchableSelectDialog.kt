package stud.euktop.uikit.components.input.select.searchable

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import stud.euktop.uikit.R
import stud.euktop.uikit.databinding.DialogSearchableSelectBinding

class SchJSearchableSelectDialog(
    private val adapter: RecyclerView.Adapter<*>,
    private val title: String,
    private val onFilterClick: (() -> Unit)?,
    private val onDismiss: () -> Unit
) : BottomSheetDialogFragment() {

    private var binding: DialogSearchableSelectBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSearchableSelectBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        binding?.apply {
            toolbar.title = title
            toolbar.setNavigationOnClickListener { dismiss() }
            if (onFilterClick != null) {
                toolbar.menu?.findItem(R.id.action_filter)?.isVisible = true
                toolbar.setOnMenuItemClickListener { menuItem ->
                    if (menuItem.itemId == R.id.action_filter) {
                        onFilterClick.invoke()
                        true
                    } else false
                }
            } else {
                toolbar.menu?.findItem(R.id.action_filter)?.isVisible = false
            }
            recyclerView.adapter = this@SchJSearchableSelectDialog.adapter
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }
}