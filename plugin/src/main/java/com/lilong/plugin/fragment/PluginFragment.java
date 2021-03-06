package com.lilong.plugin.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lilong.plugin.R;
import com.lilong.plugindemo.application.DemoApplication;
import com.lilong.plugindemo.test.Test;
import com.lilong.plugininterface.BasePluginFragment;

/**
 * 插件fragment
 * (1) 类的加载：因为这个类是插件的入口点，由自定义的DexClassLoader加载，所以其引用的所有类，其中没被主工程类加载器加载的类(也就是插件中定义的类)，也由这个DexClassLoader加载，不需专门处理
 * 这是类加载器的委托机制决定的，所以自定义的DexClassLoader的加载类的方法，实际上可以加载任何主工程和插件中的类，而主工程的类加载器的加载类的方法，只能加载主工程的类
 * (2) 资源的加载：宿主activity的contextImpl中的resources已经被换成了同时支持主工程和插件资源的resources，而且插件资源id的分段与主工程不同，无冲突，所以插件内加载插件资源是没问题的
 * 从设计思想上看插件不应该加载主工程资源，能不能做到未测试，理论上有主工程资源正确的资源id就可以
 * (3) 主题：不单独处理，一切跟原来一样，contextImpl中的mTheme不换，因为其被setTheme方法设置过成正确主题了(设置成manifest中的主题)
 */

public class PluginFragment extends BasePluginFragment {

    private View fragmentRootView;

    private TextView tvTextByXml;
    private TextView tvTextBySelfSetText;
    private TextView tvTextByExternalSetText;
    private TextView tvTextUseMainResource;

    private Button btnCallPluginProjectToast;
    private Button btnCallMainProjectToast;

    private Button btnPluginLoadMainClass;
    private Button btnPluginLoadPluginClass;

    private Button btnPluginGetDemoApplicationInstance;

    private static final String CLASS_LOADER_TEST_PLUGIN_CLASS_NAME = "com.lilong.plugin.test.Test";
    private static final String CLASS_LOADER_TEST_MAIN_CLASS_NAME = "com.lilong.plugindemo.test.Test";
    private static final String CLASS_LOADER_TEST_DEMO_APPLICATION_CLASS_NAME = "com.lilong.plugindemo.application.DemoApplication";

    @SuppressLint("ResourceType")
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentRootView = inflater.inflate(R.layout.plugin_fragment, container, false);
        tvTextByXml = (TextView) fragmentRootView.findViewById(R.id.tvTextByXml);
        tvTextBySelfSetText = (TextView) fragmentRootView.findViewById(R.id.tvTextBySelfSetText);
        tvTextByExternalSetText = (TextView) fragmentRootView.findViewById(R.id.tvTextByExternalSetText);
        btnCallPluginProjectToast = (Button) fragmentRootView.findViewById(R.id.btnCallPluginProjectToast);
        btnCallMainProjectToast = (Button) fragmentRootView.findViewById(R.id.btnCallMainProjectToast);
        btnPluginLoadMainClass = (Button) fragmentRootView.findViewById(R.id.btnPluginLoadMainClass);
        btnPluginLoadPluginClass = (Button) fragmentRootView.findViewById(R.id.btnPluginLoadPluginClass);
        btnPluginGetDemoApplicationInstance = (Button) fragmentRootView.findViewById(R.id.btnPluginGetDemoApplicationInstance);
        tvTextUseMainResource = (TextView) fragmentRootView.findViewById(R.id.tvTextUseMainResource);

        tvTextBySelfSetText.setText(R.string.tvTextBySelfSetText);
        tvTextByExternalSetText.setText(getResources().getString(R.string.tvTextByExternalSetText));
        tvTextUseMainResource.setText(0x7f109998);
        btnCallPluginProjectToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.pluginProjectToastText, Toast.LENGTH_SHORT).show();
            }
        });
        btnCallMainProjectToast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Test.showToast();
            }
        });
        btnPluginLoadMainClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Class c = getContext().getClassLoader().loadClass(CLASS_LOADER_TEST_MAIN_CLASS_NAME);
                    Toast.makeText(getActivity(), "类加载成功:" + c.getName(), Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPluginLoadPluginClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class c = null;
                try {
                    c = getContext().getClassLoader().loadClass(CLASS_LOADER_TEST_PLUGIN_CLASS_NAME);
                    Toast.makeText(getActivity(), "类加载成功:" + c.getName(), Toast.LENGTH_SHORT).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        btnPluginGetDemoApplicationInstance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Class c = null;
                try {
                    c = getContext().getClassLoader().loadClass(CLASS_LOADER_TEST_DEMO_APPLICATION_CLASS_NAME);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                DemoApplication instance = DemoApplication.getInstance();
                Toast.makeText(getActivity(), "Class object = " + c + ", \ninstance = " + instance, Toast.LENGTH_LONG).show();
            }
        });
        return fragmentRootView;
    }

    @Override
    public void callPluginMethod() {
        Toast.makeText(getActivity(), R.string.methodCallFromMainProject, Toast.LENGTH_SHORT).show();
    }
}
