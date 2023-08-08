package ma.swblockeditor.core;

import android.content.Context;
import android.util.Pair;
/*import com.besome.sketch.define.BlockBean;
import com.besome.sketch.define.ComponentBean;
import com.besome.sketch.define.EventBean;
import com.besome.sketch.define.ViewBean;
import com.besome.sketch.editor.logic.block.Block;
import com.besome.sketch.editor.src.SourceUtil;
import com.besome.sketch.lib.utils.SharedPreferenceUtil;*/
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class DesignDataManager {
    public static final int VAR_TYPE_BOOLEAN = 0;
    public static final int VAR_TYPE_INT = 1;
    public static final int VAR_TYPE_STRING = 2;
    public static boolean isInitialized = true;
    protected static Map<String, HashMap<String, ArrayList<BlockBean>>> mapBlocks;
  //  protected static Map<String, ArrayList<ComponentBean>> mapComponents;
    protected static Map<String, ArrayList<BlockBean>> mapCopiedBlocks;
 //   protected static Map<String, ArrayList<EventBean>> mapEvents;
    protected static Map<String, ArrayList<Pair<String, String>>> mapFunctions;
    protected static Map<String, ArrayList<Pair<Integer, String>>> mapLists;
    protected static Map<String, ArrayList<Pair<Integer, String>>> mapVariables;
 //   protected static Map<String, ArrayList<ViewBean>> mapViews;
    public static SharedPreferenceUtil prefLogic;
   // public static SharedPreferenceUtil prefView;

   /*
    public static void addComponent(String str, int i, String str2) {
        if (!mapComponents.containsKey(str)) {
            mapComponents.put(str, new ArrayList());
        }
        ((ArrayList) mapComponents.get(str)).add(new ComponentBean(i, str2));
    }

    public static void addComponent(String str, int i, String str2, String str3) {
        if (!mapComponents.containsKey(str)) {
            mapComponents.put(str, new ArrayList());
        }
        ((ArrayList) mapComponents.get(str)).add(new ComponentBean(i, str2, str3));
    }

    public static void addEvent(String str, int i, int i2, String str2, String str3) {
        if (!mapEvents.containsKey(str)) {
            mapEvents.put(str, new ArrayList());
        }
        ((ArrayList) mapEvents.get(str)).add(new EventBean(i, i2, str2, str3));
    }

    public static void addEvent(String str, EventBean eventBean) {
        if (!mapEvents.containsKey(str)) {
            mapEvents.put(str, new ArrayList());
        }
        ((ArrayList) mapEvents.get(str)).add(eventBean);
    }*/

    public static void addFunction(String str, String str2, String str3) {
        Pair pair = new Pair(str2, str3);
        if (!mapFunctions.containsKey(str)) {
            mapFunctions.put(str, new ArrayList());
        }
        ((ArrayList) mapFunctions.get(str)).add(pair);
    }

    public static void addList(String str, int i, String str2) {
        Pair pair = new Pair(Integer.valueOf(i), str2);
        if (!mapLists.containsKey(str)) {
            mapLists.put(str, new ArrayList());
        }
        ((ArrayList) mapLists.get(str)).add(pair);
    }

    public static void addVariable(String str, int i, String str2) {
        Pair pair = new Pair(Integer.valueOf(i), str2);
        if (!mapVariables.containsKey(str)) {
            mapVariables.put(str, new ArrayList());
        }
        ((ArrayList) mapVariables.get(str)).add(pair);
    }
/*
    public static void addView(String str, ViewBean viewBean) {
        if (!mapViews.containsKey(str)) {
            mapViews.put(str, new ArrayList());
        }
        ((ArrayList) mapViews.get(str)).add(viewBean);
    }
*/
    public static void clearClipboard(String str) {
        if (mapCopiedBlocks.containsKey(str) && ((ArrayList) mapCopiedBlocks.get(str)) != null) {
            ((ArrayList) mapCopiedBlocks.get(str)).clear();
        }
    }

    public static void copyBlocks(String str, ArrayList<Block> arrayList) {
        clearClipboard(str);
        ArrayList arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Block block = (Block) it.next();
            BlockBean blockBean = new BlockBean();
            BlockBean bean = block.getBean();
            blockBean.copy(bean);
            Object[] objArr = new Object[VAR_TYPE_INT];
            objArr[VAR_TYPE_BOOLEAN] = Integer.valueOf(bean.id);
            blockBean.id = String.format("99%06d", objArr);
            if (bean.subStack1 > 0) {
                blockBean.subStack1 = bean.subStack1 + 99000000;
            }
            if (bean.subStack2 > 0) {
                blockBean.subStack2 = bean.subStack2 + 99000000;
            }
            if (bean.nextBlock > 0) {
                blockBean.nextBlock = bean.nextBlock + 99000000;
            }
            blockBean.parameters.clear();
            Iterator it2 = bean.parameters.iterator();
            while (it2.hasNext()) {
                String str2 = (String) it2.next();
                if (str2.length() <= VAR_TYPE_INT || str2.charAt(VAR_TYPE_BOOLEAN) != '@') {
                    blockBean.parameters.add(str2);
                } else {
                    Object[] objArr2 = new Object[VAR_TYPE_INT];
                    objArr2[VAR_TYPE_BOOLEAN] = Integer.valueOf(str2.substring(VAR_TYPE_INT));
                    blockBean.parameters.add('@' + String.format("99%06d", objArr2));
                }
            }
            arrayList2.add(blockBean);
        }
        mapCopiedBlocks.put(str, arrayList2);
    }

    public static Map<String, ArrayList<BlockBean>> getAllBlocks(String str) {
        return !mapBlocks.containsKey(str) ? new HashMap() : (Map) mapBlocks.get(str);
    }

    public static ArrayList<String> getAllLists(String str) {
        ArrayList<String> arrayList = new ArrayList();
        if (mapLists.containsKey(str)) {
            ArrayList arrayList2 = (ArrayList) mapLists.get(str);
            if (arrayList2 != null) {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    arrayList.add(((Pair<Integer, String>) it.next()).second);
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<String> getAllNamesForValid(String str) {
        ArrayList<String> arrayList = new ArrayList();
        Iterator it = getVariables(str).iterator();
        while (it.hasNext()) {
            arrayList.add(((Pair<Integer, String>) it.next()).second);
        }
        it = getLists(str).iterator();
        while (it.hasNext()) {
            arrayList.add(((Pair<Integer, String>) it.next()).second);
        }
        it = getFunctions(str).iterator();
        while (it.hasNext()) {
            arrayList.add(((Pair<String, String>) it.next()).first);
        }
     /*   it = getAllViews(ProjectFileManager.getXmlNameFromJava(str)).iterator();
        while (it.hasNext()) {
            arrayList.add(SourceUtil.getVariableNameFromId(((ViewBean) it.next()).id));
        }
        it = getComponents(str).iterator();
        while (it.hasNext()) {
            arrayList.add(((ComponentBean) it.next()).componentId);
        }*/
        return arrayList;
    }
/*
    public static ArrayList<Pair<Integer, String>> getAllViewName(String str) {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) mapViews.get(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ViewBean viewBean = (ViewBean) it.next();
                if (viewBean.type != 0) {
                    arrayList.add(new Pair(Integer.valueOf(viewBean.type), viewBean.id));
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<ViewBean> getAllViews(String str) {
        ArrayList<ViewBean> arrayList = (ArrayList) mapViews.get(str);
        return arrayList == null ? new ArrayList() : arrayList;
    }
*/


    public static ArrayList<BlockBean> getBlocks(String str, String str2) {
        if (!mapBlocks.containsKey(str)) {
            return new ArrayList();
        }
        Map map = (Map) mapBlocks.get(str);
        return map == null ? new ArrayList() : !map.containsKey(str2) ? new ArrayList() : (ArrayList) map.get(str2);
    }
/*
    protected static ArrayList<ViewBean> getChildViewBeans(ArrayList<ViewBean> arrayList, ViewBean viewBean) {
        ArrayList<ViewBean> arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ViewBean viewBean2 = (ViewBean) it.next();
            if (viewBean2.parent.equals(viewBean.id)) {
                arrayList2.add(viewBean2);
            }
        }
        int size = arrayList2.size();
        for (int i = VAR_TYPE_BOOLEAN; i < size - 1; i += VAR_TYPE_INT) {
            for (int i2 = VAR_TYPE_BOOLEAN; i2 < (size - i) - 1; i2 += VAR_TYPE_INT) {
                if (((ViewBean) arrayList2.get(i2)).index > ((ViewBean) arrayList2.get(i2 + VAR_TYPE_INT)).index) {
                    viewBean2 = (ViewBean) arrayList2.get(i2);
                    arrayList2.set(i2, arrayList2.get(i2 + VAR_TYPE_INT));
                    arrayList2.set(i2 + VAR_TYPE_INT, viewBean2);
                }
            }
        }
        it = arrayList.iterator();
        while (it.hasNext()) {
            viewBean2 = (ViewBean) it.next();
            if (viewBean2.parent.equals(viewBean.id) && viewBean2.type == 0) {
                arrayList2.addAll(getChildViewBeans(arrayList, viewBean2));
            }
        }
        return arrayList2;
    }*/

    public static ArrayList<BlockBean> getClipboard(String str) {
        return !mapCopiedBlocks.containsKey(str) ? new ArrayList() : (ArrayList) mapCopiedBlocks.get(str);
    }
/*
    public static ComponentBean getComponentByIndex(String str, int i) {
        return !mapComponents.containsKey(str) ? null : (ComponentBean) ((ArrayList) mapComponents.get(str)).get(i);
    }

    public static ArrayList<ComponentBean> getComponents(String str) {
        return !mapComponents.containsKey(str) ? new ArrayList() : (ArrayList) mapComponents.get(str);
    }

    public static ArrayList<String> getComponentsByType(String str, int i) {
        ArrayList<String> arrayList = new ArrayList();
        if (mapComponents.containsKey(str)) {
            ArrayList arrayList2 = (ArrayList) mapComponents.get(str);
            if (arrayList2 != null) {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    ComponentBean componentBean = (ComponentBean) it.next();
                    if (componentBean.type == i) {
                        arrayList.add(componentBean.componentId);
                    }
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<EventBean> getEvents(String str) {
        return !mapEvents.containsKey(str) ? new ArrayList() : (ArrayList) mapEvents.get(str);
    }
*/
    public static String getFunctionSpec(String str, String str2) {
        if (!mapFunctions.containsKey(str)) {
            return "";
        }
        ArrayList arrayList = (ArrayList) mapFunctions.get(str);
        if (arrayList == null) {
            return "";
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Pair pair = (Pair) it.next();
            if (((String) pair.first).equals(str2)) {
                return (String) pair.second;
            }
        }
        return "";
    }

    public static ArrayList<Pair<String, String>> getFunctions(String str) {
        return mapFunctions.containsKey(str) ? (ArrayList) mapFunctions.get(str) : new ArrayList();
    }
/*
    public static ArrayList<Pair<Integer, String>> getListSpnName(String str) {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) mapViews.get(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ViewBean viewBean = (ViewBean) it.next();
                if (viewBean.type == 10 || viewBean.type == 9) {
                    arrayList.add(new Pair(Integer.valueOf(viewBean.type), viewBean.id));
                }
            }
        }
        return arrayList;
    }
*/
    public static ArrayList<Pair<Integer, String>> getLists(String str) {
        return mapLists.containsKey(str) ? (ArrayList) mapLists.get(str) : new ArrayList();
    }

    public static ArrayList<String> getListsByType(String str, int i) {
        ArrayList<String> arrayList = new ArrayList();
        if (mapLists.containsKey(str)) {
            ArrayList arrayList2 = (ArrayList) mapLists.get(str);
            if (arrayList2 != null) {
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    Pair<Integer, String> pair = (Pair<Integer, String>) it.next();
                    if (((Integer) pair.first).intValue() == i) {
                        arrayList.add(pair.second);
                    }
                }
            }
        }
        return arrayList;
    }

   /* public static ArrayList<Pair<Integer, String>> getTextViewName(String str) {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) mapViews.get(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ViewBean viewBean = (ViewBean) it.next();
                if (viewBean.type == 4 || viewBean.type == 3 || viewBean.type == 5 || viewBean.type == 11) {
                    arrayList.add(new Pair(Integer.valueOf(viewBean.type), viewBean.id));
                }
            }
        }
        return arrayList;
    }*/

    public static ArrayList<Pair<Integer, String>> getVariables(String str) {
        return mapVariables.containsKey(str) ? (ArrayList) mapVariables.get(str) : new ArrayList();
    }

    public static ArrayList<String> getVariablesByType(String str, int i) {
        ArrayList<String> arrayList = new ArrayList();
        Iterator it = ((ArrayList) mapVariables.get(str)).iterator();
        while (it.hasNext()) {
            Pair<Integer, String> pair = (Pair<Integer, String>) it.next();
            if (((Integer) pair.first).intValue() == i) {
                arrayList.add(pair.second);
            }
        }
        return arrayList;
    }

 /*   public static ViewBean getViewBean(String str, String str2) {
        ViewBean viewBean;
        ArrayList arrayList = (ArrayList) mapViews.get(str);
        if (arrayList == null) {
            viewBean = null;
        } else {
            int i = VAR_TYPE_BOOLEAN;
            while (i < arrayList.size()) {
                viewBean = (ViewBean) arrayList.get(i);
                if (!str2.equals(viewBean.id)) {
                    i += VAR_TYPE_INT;
                }
            }
            return null;
        }
        return viewBean;
    }*/

 /*   public static ArrayList<Pair<Integer, String>> getViewNameByType(String str, int i) {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList();
        ArrayList arrayList2 = (ArrayList) mapViews.get(str);
        if (arrayList2 != null) {
            Iterator it = arrayList2.iterator();
            while (it.hasNext()) {
                ViewBean viewBean = (ViewBean) it.next();
                if (viewBean.type == i) {
                    arrayList.add(new Pair(Integer.valueOf(viewBean.type), viewBean.id));
                }
            }
        }
        return arrayList;
    }

    public static ArrayList<ViewBean> getViews(String str, ViewBean viewBean) {
        ArrayList<ViewBean> arrayList = new ArrayList();
        arrayList.add(viewBean);
        arrayList.addAll(getChildViewBeans((ArrayList) mapViews.get(str), viewBean));
        return arrayList;
    }*/

    protected static void initMaps() {
   /*     if (mapViews != null) {
            mapViews.clear();
        }*/
        if (mapBlocks != null) {
            mapBlocks.clear();
        }
        if (mapVariables != null) {
            mapVariables.clear();
        }
        if (mapLists != null) {
            mapLists.clear();
        }
    /*    if (mapComponents != null) {
            mapComponents.clear();
        }
        if (mapEvents != null) {
            mapEvents.clear();
        }*/
      //  mapViews = new HashMap();
        mapBlocks = new HashMap();
        mapVariables = new HashMap();
        mapLists = new HashMap();
        mapFunctions = new HashMap();
  //      mapComponents = new HashMap();
    //    mapEvents = new HashMap();
        mapCopiedBlocks = new HashMap();
    }

    public static void initialize(Context context, String str) {
        initMaps();
    //    prefView = new SharedPreferenceUtil(context, "P19");
        prefLogic = new SharedPreferenceUtil(context, "P20");
        isInitialized = true;
    }

    public static boolean isExistClipboard(String str) {
        if (!mapCopiedBlocks.containsKey(str) || mapCopiedBlocks.get(str) == null) {
            return false;
        }
        return ((ArrayList) mapCopiedBlocks.get(str)).size() > 0;
    }

    public static boolean isExistDefinedBlock(String str, String str2) {
        HashMap<String, ArrayList<BlockBean>> map = mapBlocks.get(str);
        if (map == null) {
            return false;
        }
        for (Entry entry : map.entrySet()) {
            if (!((String) entry.getKey()).equals(str2 + "_" + "moreBlock")) {
                Iterator it = ((ArrayList) entry.getValue()).iterator();
                while (it.hasNext()) {
                    BlockBean blockBean = (BlockBean) it.next();
                    if (blockBean.opCode.equals("definedFunc")) {
                        int indexOf = blockBean.spec.indexOf(" ");
                        if ((indexOf > 0 ? blockBean.spec.substring(VAR_TYPE_BOOLEAN, indexOf) : blockBean.spec).equals(str2)) {
                            return true;
                        }
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static boolean isExistListBlock(String str, String str2, String str3) {
        HashMap<String, ArrayList<BlockBean>> map = mapBlocks.get(str);
        if (map == null) {
            return false;
        }
        for (Entry entry : map.entrySet()) {
            if (!((String) entry.getKey()).equals(str3)) {
                Iterator it = ((ArrayList) entry.getValue()).iterator();
                while (it.hasNext()) {
                    BlockBean blockBean = (BlockBean) it.next();
                    String str4 = blockBean.opCode;
                    int i = -1;
                    switch (str4.hashCode()) {
                        case -1384861688:
                            if (str4.equals("getAtListInt")) {
                                i = 6;
                                break;
                            }
                            break;
                        case -1384851894:
                            if (str4.equals("getAtListStr")) {
                                i = 7;
                                break;
                            }
                            break;
                        case -1271141237:
                            if (str4.equals("clearList")) {
                                i = 3;
                                break;
                            }
                            break;
                        case -329562760:
                            if (str4.equals("insertListInt")) {
                                i = 11;
                                break;
                            }
                            break;
                        case -329552966:
                            if (str4.equals("insertListStr")) {
                                i = 12;
                                break;
                            }
                            break;
                        case -96313603:
                            if (str4.equals("containListInt")) {
                                i = VAR_TYPE_INT;
                                break;
                            }
                            break;
                        case -96303809:
                            if (str4.equals("containListStr")) {
                                i = VAR_TYPE_STRING;
                                break;
                            }
                            break;
                        case 762282303:
                            if (str4.equals("indexListInt")) {
                                i = 8;
                                break;
                            }
                            break;
                        case 762292097:
                            if (str4.equals("indexListStr")) {
                                i = 9;
                                break;
                            }
                            break;
                        case 1160674468:
                            if (str4.equals("lengthList")) {
                                i = VAR_TYPE_BOOLEAN;
                                break;
                            }
                            break;
                        case 1764351209:
                            if (str4.equals("deleteList")) {
                                i = 10;
                                break;
                            }
                            break;
                        case 2090179216:
                            if (str4.equals("addListInt")) {
                                i = 4;
                                break;
                            }
                            break;
                        case 2090189010:
                            if (str4.equals("addListStr")) {
                                i = 5;
                                break;
                            }
                            break;
                    }
                    switch (i) {
                        case VAR_TYPE_BOOLEAN /*0*/:
                        case VAR_TYPE_INT /*1*/:
                        case VAR_TYPE_STRING /*2*/:
                        case 3:
                            if (!((String) blockBean.parameters.get(VAR_TYPE_BOOLEAN)).equals(str2)) {
                                break;
                            }
                            return true;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            if (!((String) blockBean.parameters.get(VAR_TYPE_INT)).equals(str2)) {
                                break;
                            }
                            return true;
                        case 11:
                        case 12:
                            if (!((String) blockBean.parameters.get(VAR_TYPE_STRING)).equals(str2)) {
                                break;
                            }
                            return true;
                        default:
                            break;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static boolean isExistVariableBlock(String str, String str2, String str3) {
        HashMap<String, ArrayList<BlockBean>> map = mapBlocks.get(str);
        if (map == null) {
            return false;
        }
        for (Entry entry : map.entrySet()) {
            if (!((String) entry.getKey()).equals(str3)) {
                Iterator it = ((ArrayList) entry.getValue()).iterator();
                while (it.hasNext()) {
                    BlockBean blockBean = (BlockBean) it.next();
                    String str4 = blockBean.opCode;
                    int i = -1;
                    switch (str4.hashCode()) {
                        case -1920517885:
                            if (str4.equals("setVarBoolean")) {
                                i = VAR_TYPE_INT;
                                break;
                            }
                            break;
                        case -1377080719:
                            if (str4.equals("decreaseInt")) {
                                i = 5;
                                break;
                            }
                            break;
                        case -1249347599:
                            if (str4.equals("getVar")) {
                                i = VAR_TYPE_BOOLEAN;
                                break;
                            }
                            break;
                        case 657721930:
                            if (str4.equals("setVarInt")) {
                                i = VAR_TYPE_STRING;
                                break;
                            }
                            break;
                        case 754442829:
                            if (str4.equals("increaseInt")) {
                                i = 4;
                                break;
                            }
                            break;
                        case 845089750:
                            if (str4.equals("setVarString")) {
                                i = 3;
                                break;
                            }
                            break;
                    }
                    switch (i) {
                        case VAR_TYPE_BOOLEAN /*0*/:
                            if (!blockBean.spec.equals(str2)) {
                                break;
                            }
                            return true;
                        case VAR_TYPE_INT /*1*/:
                        case VAR_TYPE_STRING /*2*/:
                        case 3:
                        case 4:
                        case 5:
                            if (!((String) blockBean.parameters.get(VAR_TYPE_BOOLEAN)).equals(str2)) {
                                break;
                            }
                            return true;
                        default:
                            break;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static void loadSavedLogic() {
        HashMap<String, Object> readState = prefLogic.readState();
        if (readState != null && readState.size() > 0) {
            for (Entry entry : readState.entrySet()) {
                String str = (String) entry.getKey();
                int indexOf = str.indexOf("_");
                if (indexOf >= 0) {
                    String substring = str.substring(VAR_TYPE_BOOLEAN, indexOf);
                    String substring2 = str.substring(indexOf + VAR_TYPE_INT);
                    String str2 = (String) entry.getValue();
                    if (str2 != null && str2.length() > 0) {
                        int i = -1;
                        switch (substring2.hashCode()) {
                            case -1291329255:
                                if (substring2.equals("events")) {
                                    i = 4;
                                    break;
                                }
                                break;
                            case -447446250:
                                if (substring2.equals("components")) {
                                    i = 3;
                                    break;
                                }
                                break;
                            case 116519:
                                if (substring2.equals("var")) {
                                    i = VAR_TYPE_BOOLEAN;
                                    break;
                                }
                                break;
                            case 3154628:
                                if (substring2.equals("func")) {
                                    i = VAR_TYPE_STRING;
                                    break;
                                }
                                break;
                            case 3322014:
                                if (substring2.equals("list")) {
                                    i = VAR_TYPE_INT;
                                    break;
                                }
                                break;
                        }
                        switch (i) {
                            case VAR_TYPE_BOOLEAN /*0*/:
                                mapVariables.put(substring, parseVariableString(str2));
                                break;
                            case VAR_TYPE_INT /*1*/:
                                mapLists.put(substring, parseListString(str2));
                                break;
                            case VAR_TYPE_STRING /*2*/:
                                mapFunctions.put(substring, parseFunctionString(str2));
                                break;
                            case 3:
                           //     mapComponents.put(substring, parseJsonToComponentArray(str2));
                                break;
                            case 4:
                      //          mapEvents.put(substring, parseJsonToEventArray(str2));
                                break;
                            default:
                                if (!mapBlocks.containsKey(substring)) {
                                    mapBlocks.put(substring, new HashMap());
                                }
                                ((HashMap) mapBlocks.get(substring)).put(substring2, parseJsonToBlockArray(str2));
                                break;
                        }
                    }
                }
            }
        }
    }

  /*  public static void loadSavedView() {
        Gson gson = new Gson();
        HashMap readState = prefView.readState();
        if (readState != null && readState.size() > 0) {
            for (Entry entry : readState.entrySet()) {
                String str = (String) entry.getValue();
                ArrayList arrayList = new ArrayList();
                while (true) {
                    int indexOf = str.indexOf("\n");
                    if (indexOf <= 0 || str.charAt(VAR_TYPE_BOOLEAN) != '{') {
                        break;
                    }
                    arrayList.add(gson.fromJson(str.substring(VAR_TYPE_BOOLEAN, indexOf), ViewBean.class));
                    if (indexOf >= str.length() - 1) {
                        break;
                    }
                    str = str.substring(indexOf + VAR_TYPE_INT);
                }
                mapViews.put(entry.getKey(), arrayList);
            }
        }
    }*/

    public static ArrayList<Pair<String, String>> parseFunctionString(String str) {
        ArrayList<Pair<String, String>> arrayList = new ArrayList();
        while (true) {
            int indexOf = str.indexOf("\n");
            if (indexOf > 0) {
                String trim = str.substring(VAR_TYPE_BOOLEAN, indexOf).trim();
                if (trim.length() > 0 && trim.indexOf(":") >= 0) {
                    arrayList.add(new Pair(trim.substring(VAR_TYPE_BOOLEAN, trim.indexOf(":")), trim.substring(trim.indexOf(":") + VAR_TYPE_INT)));
                    if (indexOf >= str.length() - 1) {
                        break;
                    }
                    str = str.substring(indexOf + VAR_TYPE_INT);
                } else {
                    str = str.substring(indexOf + VAR_TYPE_INT);
                }
            } else {
                break;
            }
        }
        return arrayList;
    }

    protected static ArrayList<BlockBean> parseJsonToBlockArray(String str) {
        Gson gson = new Gson();
        ArrayList<BlockBean> arrayList = new ArrayList();
        while (str != null && str.length() > 0) {
            int indexOf = str.indexOf("\n");
            if (indexOf <= 0 || str.charAt(VAR_TYPE_BOOLEAN) != '{') {
                break;
            }
            arrayList.add(gson.fromJson(str.substring(VAR_TYPE_BOOLEAN, indexOf).trim(), BlockBean.class));
            if (indexOf >= str.length() - 1) {
                break;
            }
            str = str.substring(indexOf + VAR_TYPE_INT);
        }
        return arrayList;
    }

  /*  protected static ArrayList<ComponentBean> parseJsonToComponentArray(String str) {
        Gson gson = new Gson();
        ArrayList<ComponentBean> arrayList = new ArrayList();
        while (str != null && str.length() > 0) {
            int indexOf = str.indexOf("\n");
            if (indexOf <= 0 || str.charAt(VAR_TYPE_BOOLEAN) != '{') {
                break;
            }
            arrayList.add(gson.fromJson(str.substring(VAR_TYPE_BOOLEAN, indexOf).trim(), ComponentBean.class));
            if (indexOf >= str.length() - 1) {
                break;
            }
            str = str.substring(indexOf + VAR_TYPE_INT);
        }
        return arrayList;
    }

    protected static ArrayList<EventBean> parseJsonToEventArray(String str) {
        Gson gson = new Gson();
        ArrayList<EventBean> arrayList = new ArrayList();
        while (str != null && str.length() > 0) {
            int indexOf = str.indexOf("\n");
            if (indexOf <= 0 || str.charAt(VAR_TYPE_BOOLEAN) != '{') {
                break;
            }
            arrayList.add(gson.fromJson(str.substring(VAR_TYPE_BOOLEAN, indexOf).trim(), EventBean.class));
            if (indexOf >= str.length() - 1) {
                break;
            }
            str = str.substring(indexOf + VAR_TYPE_INT);
        }
        return arrayList;
    }*/

    public static ArrayList<Pair<Integer, String>> parseListString(String str) {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList();
        while (true) {
            int indexOf = str.indexOf("\n");
            if (indexOf > 0) {
                String trim = str.substring(VAR_TYPE_BOOLEAN, indexOf).trim();
                if (trim.length() > 0 && trim.indexOf(":") >= 0) {
                    String substring = trim.substring(VAR_TYPE_BOOLEAN, trim.indexOf(":"));
                    arrayList.add(new Pair(Integer.valueOf(substring), trim.substring(trim.indexOf(":") + VAR_TYPE_INT)));
                    if (indexOf >= str.length() - 1) {
                        break;
                    }
                    str = str.substring(indexOf + VAR_TYPE_INT);
                } else {
                    str = str.substring(indexOf + VAR_TYPE_INT);
                }
            } else {
                break;
            }
        }
        return arrayList;
    }

    public static ArrayList<Pair<Integer, String>> parseVariableString(String str) {
        ArrayList<Pair<Integer, String>> arrayList = new ArrayList();
        while (true) {
            int indexOf = str.indexOf("\n");
            if (indexOf > 0) {
                String trim = str.substring(VAR_TYPE_BOOLEAN, indexOf).trim();
                if (trim.length() > 0 && trim.indexOf(":") >= 0) {
                    String substring = trim.substring(VAR_TYPE_BOOLEAN, trim.indexOf(":"));
                    arrayList.add(new Pair(Integer.valueOf(substring), trim.substring(trim.indexOf(":") + VAR_TYPE_INT)));
                    if (indexOf >= str.length() - 1) {
                        break;
                    }
                    str = str.substring(indexOf + VAR_TYPE_INT);
                } else {
                    str = str.substring(indexOf + VAR_TYPE_INT);
                }
            } else {
                break;
            }
        }
        return arrayList;
    }

/*    public static void printData(String str) {
        Iterator it = ((ArrayList) mapViews.get(str)).iterator();
        while (it.hasNext()) {
            ((ViewBean) it.next()).print();
        }
    }*/

    public static void removeBlocks(String str, String str2) {
        if (mapBlocks.containsKey(str)) {
            Map map = (Map) mapBlocks.get(str);
            if (map != null && map.containsKey(str2)) {
                map.remove(str2);
            }
        }
    }
/*
    public static void removeComponent(String str, String str2) {
        if (mapComponents.containsKey(str)) {
            ArrayList arrayList = (ArrayList) mapComponents.get(str);
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ComponentBean componentBean = (ComponentBean) it.next();
                if (componentBean.componentId.equals(str2)) {
                    arrayList.remove(componentBean);
                    break;
                }
            }
            if (mapBlocks.containsKey(str)) {
                for (Entry value : ((Map) mapBlocks.get(str)).entrySet()) {
                    Iterator it2 = ((ArrayList) value.getValue()).iterator();
                    while (it2.hasNext()) {
                        BlockBean blockBean = (BlockBean) it2.next();
                        for (int i = VAR_TYPE_BOOLEAN; i < blockBean.paramTypes.size(); i += VAR_TYPE_INT) {
                            String str3 = (String) blockBean.parameters.get(i);
                            if (((String) blockBean.paramTypes.get(i)).equals("m") && str3.equals(str2)) {
                                blockBean.parameters.set(i, "");
                            }
                        }
                    }
                }
            }
        }
    }

    public static void removeEvent(String str, String str2) {
        if (mapEvents.containsKey(str)) {
            ArrayList arrayList = (ArrayList) mapEvents.get(str);
            if (arrayList != null) {
                int size = arrayList.size();
                while (true) {
                    int i = size - 1;
                    if (i >= 0) {
                        EventBean eventBean = (EventBean) arrayList.get(i);
                        if (eventBean.targetId.equals(str2)) {
                            arrayList.remove(eventBean);
                            if (getAllBlocks(str).containsKey(eventBean.targetId + "_" + eventBean.eventName)) {
                                getAllBlocks(str).remove(eventBean.targetId + "_" + eventBean.eventName);
                                size = i;
                            }
                        }
                        size = i;
                    } else {
                        return;
                    }
                }
            }
        }
    }
*/
    public static void removeFunction(String str, String str2) {
        if (mapFunctions.containsKey(str)) {
            ArrayList arrayList = (ArrayList) mapFunctions.get(str);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Pair pair = (Pair) it.next();
                    if (((String) pair.first).equals(str2)) {
                        arrayList.remove(pair);
                        break;
                    }
                }
                if (((HashMap) mapBlocks.get(str)).containsKey(str2 + "_" + "moreBlock")) {
                    ((HashMap) mapBlocks.get(str)).remove(str2 + "_" + "moreBlock");
                }
            }
        }
    }

    public static void removeList(String str, String str2) {
        if (mapLists.containsKey(str)) {
            ArrayList arrayList = (ArrayList) mapLists.get(str);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Pair pair = (Pair) it.next();
                    if (((String) pair.second).equals(str2)) {
                        arrayList.remove(pair);
                        return;
                    }
                }
            }
        }
    }

    public static void removeVariable(String str, String str2) {
        if (mapVariables.containsKey(str)) {
            ArrayList arrayList = (ArrayList) mapVariables.get(str);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Pair pair = (Pair) it.next();
                    if (((String) pair.second).equals(str2)) {
                        arrayList.remove(pair);
                        return;
                    }
                }
            }
        }
    }

/*    public static void removeView(String str, ViewBean viewBean) {
        if (mapViews.containsKey(str)) {
            ((ArrayList) mapViews.get(str)).remove(viewBean);
            String javaNameFromXml = ProjectFileManager.getJavaNameFromXml(str);
            if (mapBlocks.containsKey(javaNameFromXml)) {
                Map map = (Map) mapBlocks.get(javaNameFromXml);
                if (map != null && viewBean.type != 0) {
                    for (Entry value : map.entrySet()) {
                        ArrayList arrayList = (ArrayList) value.getValue();
                        if (arrayList != null && arrayList.size() > 0) {
                            Iterator it = arrayList.iterator();
                            while (it.hasNext()) {
                                BlockBean blockBean = (BlockBean) it.next();
                                for (int i = VAR_TYPE_BOOLEAN; i < blockBean.paramTypes.size(); i += VAR_TYPE_INT) {
                                    String str2 = (String) blockBean.parameters.get(i);
                                    if (((String) blockBean.paramTypes.get(i)).equals("m") && str2.equals(viewBean.id)) {
                                        blockBean.parameters.set(i, "");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<ViewBean> reorderViewBeans(ArrayList<ViewBean> arrayList) {
        ArrayList<ViewBean> arrayList2 = new ArrayList();
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            ViewBean viewBean = (ViewBean) it.next();
            if (viewBean.parent.equals("root")) {
                arrayList2.add(viewBean);
            }
        }
        int size = arrayList2.size();
        for (int i = VAR_TYPE_BOOLEAN; i < size - 1; i += VAR_TYPE_INT) {
            for (int i2 = VAR_TYPE_BOOLEAN; i2 < (size - i) - 1; i2 += VAR_TYPE_INT) {
                if (((ViewBean) arrayList2.get(i2)).index > ((ViewBean) arrayList2.get(i2 + VAR_TYPE_INT)).index) {
                    viewBean = (ViewBean) arrayList2.get(i2);
                    arrayList2.set(i2, arrayList2.get(i2 + VAR_TYPE_INT));
                    arrayList2.set(i2 + VAR_TYPE_INT, viewBean);
                }
            }
        }
        it = arrayList.iterator();
        while (it.hasNext()) {
            viewBean = (ViewBean) it.next();
            if (viewBean.type == 0 && viewBean.parent.equals("root")) {
                arrayList2.addAll(getChildViewBeans(arrayList, viewBean));
            }
        }
        return arrayList2;
    }*/

  /* public static void saveProject() {
        Iterator it;
        ArrayList arrayList;
        String str;
        String str2;
        prefView.clearAll();
        prefLogic.clearAll();
        Gson gson = new Gson();
        if (mapViews != null && mapViews.size() > 0) {
            for (Entry entry : mapViews.entrySet()) {
                arrayList = (ArrayList) entry.getValue();
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList reorderViewBeans = reorderViewBeans((ArrayList) entry.getValue());
                    Object obj = "";
                    if (reorderViewBeans != null && reorderViewBeans.size() > 0) {
                        str = obj;
                        for (int i = VAR_TYPE_BOOLEAN; i < reorderViewBeans.size(); i += VAR_TYPE_INT) {
                            str = str + gson.toJson((ViewBean) reorderViewBeans.get(i)) + "\n";
                        }
                        obj = str;
                    }
                    prefView.putObject((String) entry.getKey(), obj);
                }
            }
            prefView.commit();
        }
        if (mapVariables != null && mapVariables.size() > 0) {
            for (Entry entry2 : mapVariables.entrySet()) {
                arrayList = (ArrayList) entry2.getValue();
                if (arrayList != null && arrayList.size() > 0) {
                    str2 = "";
                    it = arrayList.iterator();
                    while (it.hasNext()) {
                        Pair pair = (Pair) it.next();
                        str2 = str2 + pair.first + ":" + ((String) pair.second) + "\n";
                    }
                    prefLogic.putObject(((String) entry2.getKey()) + "_" + "var", str2);
                }
            }
        }
        if (mapLists != null && mapLists.size() > 0) {
            for (Entry entry22 : mapLists.entrySet()) {
                arrayList = (ArrayList) entry22.getValue();
                if (arrayList != null && arrayList.size() > 0) {
                    str2 = "";
                    it = arrayList.iterator();
                    while (it.hasNext()) {
                        pair = (Pair) it.next();
                        str2 = str2 + pair.first + ":" + ((String) pair.second) + "\n";
                    }
                    prefLogic.putObject(((String) entry22.getKey()) + "_" + "list", str2);
                }
            }
        }
        if (mapFunctions != null && mapFunctions.size() > 0) {
            for (Entry entry222 : mapFunctions.entrySet()) {
                arrayList = (ArrayList) entry222.getValue();
                if (arrayList != null && arrayList.size() > 0) {
                    str2 = "";
                    it = arrayList.iterator();
                    while (it.hasNext()) {
                        pair = (Pair) it.next();
                        str2 = str2 + ((String) pair.first) + ":" + ((String) pair.second) + "\n";
                    }
                    prefLogic.putObject(((String) entry222.getKey()) + "_" + "func", str2);
                }
            }
        }
        if (mapComponents != null && mapComponents.size() > 0) {
            for (Entry entry2222 : mapComponents.entrySet()) {
                arrayList = (ArrayList) entry2222.getValue();
                if (arrayList != null && arrayList.size() > 0) {
                    str2 = "";
                    it = arrayList.iterator();
                    while (it.hasNext()) {
                        str2 = str2 + gson.toJson((ComponentBean) it.next()) + "\n";
                    }
                    prefLogic.putObject(((String) entry2222.getKey()) + "_" + "components", str2);
                }
            }
        }
        if (mapEvents != null && mapEvents.size() > 0) {
            for (Entry entry22222 : mapEvents.entrySet()) {
                arrayList = (ArrayList) entry22222.getValue();
                if (arrayList != null && arrayList.size() > 0) {
                    str2 = "";
                    it = arrayList.iterator();
                    
                    while (it.hasNext()) {
                        str2 = str2 + gson.toJson((EventBean) it.next()) + "\n";
                    }
                    prefLogic.putObject(((String) entry22222.getKey()) + "_" + "events", str2);
                }
            }
        }
        if (mapBlocks != null && mapBlocks.size() > 0) {
            for (Entry entry222222 : mapBlocks.entrySet()) {
                String str3 = (String) entry222222.getKey();
                HashMap hashMap = (HashMap) entry222222.getValue();
                if (hashMap != null && hashMap.size() > 0) {
                    for (Entry entry2222222 : hashMap.entrySet()) {
                        ArrayList arrayList2 = (ArrayList) entry2222222.getValue();
                        if (arrayList2 != null && arrayList2.size() > 0) {
                            str = "";
                            Iterator it2 = arrayList2.iterator();
                            while (it2.hasNext()) {
                                str = str + gson.toJson((BlockBean) it2.next()) + "\n";
                            }
                            prefLogic.putObject(str3 + "_" + ((String) entry2222222.getKey()), str);
                        }
                    }
                }
            }
        }
        prefLogic.commit();
    }*/

    public static void setBlocks(String str, String str2, ArrayList<BlockBean> arrayList) {
        if (!mapBlocks.containsKey(str)) {
            mapBlocks.put(str, new HashMap());
        }
        ((Map) mapBlocks.get(str)).put(str2, arrayList);
    }
}
