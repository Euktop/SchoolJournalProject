package stud.euktop.uikit.components.input.select.def

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.components.bottomsheet.SchJBottomSheet
import stud.euktop.uikit.databinding.ItemListContentBinding

class SchJSelectSheet<T>(
    private val adapter: SchJTextAdapter<T>
) : SchJBottomSheet() {
    private var binding: ItemListContentBinding? = null
    override fun provideContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemListContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.root?.apply {
            this.adapter = this@SchJSelectSheet.adapter
        }
    }

    /**
     * Обновляет список элементов (вызывается из ViewModel после загрузки новых данных).
     */
    fun updateItems(newItems: List<T>) {
        adapter.submitList(newItems) {
            binding?.root?.update()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}