package ${packageName};
import android.view.View;
import com.wuwind.ui.base.OnClick;
import com.wuwind.ui.base.ViewDelegate;
import ${packageName}.${firstActivityName}View.Listener;

public class ${firstActivityName}View extends ViewDelegate<Listener> implements View.OnClickListener  {

    @Override
    public int getRootLayoutId() {
	<#if generateLayout>
        return R.layout.${layoutName};
	<#else>
		return 0;
	</#if>    
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void onClick(View v) {

    }

    public interface Listener extends OnClick {

    }
}