package stud.euktop.schooljournal.presentation.main.admin.audit_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import stud.euktop.schooljournal.databinding.FragmentAuditLogDetailBinding
import stud.euktop.schooljournal.presentation.common.base.BaseFragment
import stud.euktop.schooljournal.presentation.common.navigate.contract.RouterAdmin
import stud.euktop.uikit.R
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class AuditLogDetailFragment :
    BaseFragment<FragmentAuditLogDetailBinding, AuditLogDetailViewModel, AuditLogDetailState, Unit>() {

    override val viewModel: AuditLogDetailViewModel by viewModels()
    private val dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm:ss")
        .withZone(ZoneId.systemDefault())

    @Inject
    lateinit var router: RouterAdmin

    override fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentAuditLogDetailBinding.inflate(inflater, container, false)

    override fun setupUI() {

    }

    override fun updateState(state: AuditLogDetailState) {
        val log = state.log ?: return
        with(binding) {
            tvId.text = log.id.toString()
            tvActionType.text = log.actionType.name
            setActionTypeColor(tvActionType, log.actionType)

            tvTableName.text = log.tableName
            tvObjectId.text = log.objectId.toString()
            tvExecutor.text = "${log.executorName} (UID: ${log.executorUserId})"
            tvEventTime.text = dateFormatter.format(log.eventTime)
            tvIpAddress.text = log.ipAddress

            tvOldValue.text = formatMap(log.dataChange.oldValue)
            tvNewValue.text = formatMap(log.dataChange.newValue)

            tvUserAgent.text = log.userAgent
            tvRequestMethod.text = log.requestMethod
            tvOrigin.text = log.origin
        }
    }

    private fun setActionTypeColor(
        textView: android.widget.TextView,
        actionType: stud.euktop.domain.model.audit.ActionType
    ) {
        val (bgRes, textColorRes) = when (actionType) {
            stud.euktop.domain.model.audit.ActionType.CREATE -> R.drawable.bg_chip_primary to R.color.color_text_on_accent
            stud.euktop.domain.model.audit.ActionType.UPDATE -> R.drawable.bg_chip_secondary to R.color.color_text_primary
            stud.euktop.domain.model.audit.ActionType.DELETE -> R.drawable.bg_chip_error to R.color.color_text_on_accent
            stud.euktop.domain.model.audit.ActionType.LOGIN -> R.drawable.bg_chip_tertiary to R.color.color_text_on_accent
        }
        textView.setBackgroundResource(bgRes)
        textView.setTextColor(requireContext().getColor(textColorRes))
    }

    private fun formatMap(map: Map<String, Any?>): String {
        if (map.isEmpty()) return "—"
        return map.entries.joinToString(separator = "\n") { (key, value) ->
            "  \"$key\": ${formatValue(value)}"
        }
    }

    private fun formatValue(value: Any?): String = when (value) {
        null -> "null"
        is String -> "\"$value\""
        else -> value.toString()
    }

    override fun updateEvent(event: Unit) {}
}