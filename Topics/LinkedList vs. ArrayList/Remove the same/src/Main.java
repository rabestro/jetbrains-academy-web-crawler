import java.util.*;

class ListOperations {
    public static void removeTheSame(LinkedList<String> linkedList, ArrayList<String> arrayList) {
        var al = arrayList.iterator();
        for (var ll = linkedList.iterator(); ll.hasNext(); )
            if (ll.next().equals(al.next())) {
                ll.remove();
                al.remove();
            }
    }
}