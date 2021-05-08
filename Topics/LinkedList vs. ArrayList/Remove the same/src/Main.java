import java.util.*;

class ListOperations {
    public static void removeTheSame(LinkedList<String> linkedList, ArrayList<String> arrayList) {
        var al = arrayList.iterator();
        var ll = linkedList.iterator();
        while (ll.hasNext()) {
            if (ll.next().equals(al.next())) {
                ll.remove();
                al.remove();
            }
        }
    }
}