package stud.euktop.uikit.components.input.select.def

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import stud.euktop.uikit.components.bottomsheet.SchJBottomSheet
import stud.euktop.uikit.databinding.ItemListContentBinding

class SchJSelectSheet(
    private val adapter: RecyclerView.Adapter<*>,
    private val onDismiss: () -> Unit
) : SchJBottomSheet() {

    private var binding: ItemListContentBinding? = null

    override fun onDismiss(dialog: android.content.DialogInterface) {
        super.onDismiss(dialog)
        onDismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemListContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.root?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}