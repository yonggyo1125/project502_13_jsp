package org.choongang.global.config.containers;

import org.choongang.global.config.annotations.Component;
import org.choongang.global.config.annotations.Controller;
import org.choongang.global.config.annotations.RestController;
import org.choongang.global.config.annotations.Service;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BeanContainer {
    private static BeanContainer instance;

    private Map<String, Object> beans;


    public BeanContainer() {
        beans = new HashMap<>();
    }

    public void loadBeans(String packageName) {
        // 패키지 경로 기준으로 스캔 파일 경로 조회
        try {
            String rootPath = new File("./src/main/java/" + packageName.replaceAll("\\.", "/")).getCanonicalPath();
            List<Class> classNames = getClassNames(rootPath, packageName);

            for (Class clazz : classNames) {
                // 인터페이스는 동적 객체 생성을 하지 않으므로 건너띄기
                if (clazz.isInterface()) {
                    continue;
                }

                // 애노테이션 중 Controller, RestController, Component, Service 등이 TYPE 애노테이션으로 정의된 경우 beans 컨테이너에 객체 생성하여 보관
                // 키값은 전체 클래스명, 값은 생성된 객체
                String key = clazz.getName();

                // 이미 생성된 객체라면 생성된 객체로 활용
                if (beans.containsKey(key)) continue;;


                Annotation[] annotations = clazz.getDeclaredAnnotations();

                boolean isBean = false;
                for (Annotation anno : annotations) {
                    if (anno instanceof Controller || anno instanceof RestController || anno instanceof Service || anno instanceof Component)  {
                        isBean = true;
                        break;
                    }
                }
                // 컨테이너가 관리할 객체라면 생성자 매개변수의 의존성을 체크하고 의존성이 있다면 해당 객체를 생성하고 의존성을 해결한다.
                if (isBean) {
                    Constructor con = clazz.getDeclaredConstructors()[0];
                    List<Object> objs = resolveDependencies(key, con);
                    if (con.getParameterTypes().length > 0 && !beans.containsKey(key)) { // 생성자 매개변수가 있는 경우는 자동 생성되지 않았으므로 빈에 추가
                        Object obj = con.newInstance(objs.toArray());
                        beans.put(key, obj);
                    }
                }

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BeanContainer getInstance() {
        if (instance == null) {
            instance = new BeanContainer();
        }

        return instance;
    }

    /**
     * 생성된 객체 조회
     *
     * @param clazz
     * @return
     */
    public Object getBean(Class clazz) {
        return beans.get(clazz.getName());
    }

    /**
     * 의존성의 의존성을 재귀적으로 체크하여 필요한 의존성의 객체를 모두 생성한다.
     *
     * @param con
     */
    private List<Object> resolveDependencies(String key, Constructor con) throws Exception {
        List<Object> dependencies = new ArrayList<>();
        if (beans.containsKey(key)) {
            dependencies.add(beans.get(key));
            return dependencies;
        }

        Class[] parameters = con.getParameterTypes();
        if (parameters.length == 0) {
            Object obj = con.newInstance();
            dependencies.add(obj);
        } else {
            for(Class clazz : parameters) {
                Object obj = beans.get(clazz.getName());
                if (obj == null) {
                    Constructor _con = clazz.getDeclaredConstructors()[0];
                    if (_con.getParameterTypes().length == 0) {
                        obj = _con.newInstance();
                    } else {
                        List<Object> deps = resolveDependencies(clazz.getName(), _con);
                        obj = _con.newInstance(deps.toArray());
                    }
                }
                dependencies.add(obj);
            }
        }


        return dependencies;
    }

    private List<Class> getClassNames(String rootPath, String packageName) {
        List<Class> classes = new ArrayList<>();
        List<File> files = getFiles(rootPath);
        for (File file : files) {
            String path = file.getAbsolutePath();
            String className = packageName + "." + path.replace(rootPath + File.separator, "").replace(".java", "").replace(File.separator, ".");
            try {
                Class cls = Class.forName(className);
                classes.add(cls);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return classes;
    }

    private List<File> getFiles(String rootPath) {
        List<File> items = new ArrayList<>();
        File[] files = new File(rootPath).listFiles();
        if (files == null) return items;

        for (File file : files) {
            if (file.isDirectory()) {
                List<File> _files = getFiles(file.getAbsolutePath());
                if (!_files.isEmpty()) items.addAll(_files);
            } else {
                items.add(file);
            }
        }
        return items;
    }
}
