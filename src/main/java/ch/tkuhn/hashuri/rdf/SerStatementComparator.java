package ch.tkuhn.hashuri.rdf;

import java.util.Comparator;

import org.openrdf.model.BNode;
import org.openrdf.model.Literal;
import org.openrdf.model.Resource;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.impl.ContextStatementImpl;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.StatementImpl;
import org.openrdf.model.impl.URIImpl;

public class SerStatementComparator implements Comparator<String> {

	public SerStatementComparator() {
	}

	@Override
	public int compare(String s1, String s2) {
		String[] parts1 = s1.split("\\t");
		String[] parts2 = s2.split("\\t");
		int c;
		c = unescape(parts1[0]).compareTo(unescape(parts2[0]));
		if (c != 0) return c;
		c = unescape(parts1[1]).compareTo(unescape(parts2[1]));
		if (c != 0) return c;
		c = unescape(parts1[2]).compareTo(unescape(parts2[2]));
		if (c != 0) return c;
		c = unescape(parts1[3]).compareTo(unescape(parts2[3]));
		if (c != 0) return c;
		String o1 = "  ";
		if (parts1.length > 4) o1 = parts1[4];
		String o2 = "  ";
		if (parts2.length > 4) o2 = parts2[4];
		int i11 = o1.indexOf(32);
		int i21 = o2.indexOf(32);
		String d1 = unescape(o1.substring(0, i11));
		String d2 = unescape(o2.substring(0, i11));
		c = d1.compareTo(d2);
		if (c != 0) return c;
		int i12 = o1.indexOf(32, i11+1);
		int i22 = o2.indexOf(32, i21+1);
		String l1 = unescape(o1.substring(i11+1, i12));
		String l2 = unescape(o2.substring(i21+1, i22));
		c = l1.compareTo(l2);
		if (c != 0) return c;
		String v1 = unescape(o1.substring(i12+1));
		String v2 = unescape(o2.substring(i22+1));
		return v1.compareTo(v2);
	}

	public static Statement fromString(String string) {
		String[] parts = string.split("\\t");
		Resource context = null;
		if (!parts[0].isEmpty()) {
			context = new URIImpl(unescape(parts[0]));
		}
		Resource subj = new URIImpl(unescape(parts[1]));
		URI pred = new URIImpl(unescape(parts[2]));
		Value obj = null;
		if (!parts[3].isEmpty()) {
			obj = new URIImpl(unescape(parts[3]));
		} else {
			String o = parts[4];
			int i1 = o.indexOf(32);
			int i2 = o.indexOf(32, i1+1);
			String d = unescape(o.substring(0, i1));
			String l = unescape(o.substring(i1+1, i2));
			String v = unescape(o.substring(i2+1));
			if (!d.isEmpty()) {
				obj = new LiteralImpl(v, new URIImpl(d));
			} else if (!l.isEmpty()) {
				obj = new LiteralImpl(v, l);
			} else {
				obj = new LiteralImpl(v);
			}
		}
		if (context == null) {
			return new StatementImpl(subj, pred, obj);
		} else {
			return new ContextStatementImpl(subj, pred, obj, context);
		}
	}

	public static String toString(Statement st) {
		StringBuffer sb = new StringBuffer();
		Resource context = st.getContext();
		if (context instanceof BNode) {
			throw new RuntimeException("Unexpected blank node");
		} else if (context == null) {
			sb.append("\t");
		} else {
			sb.append(escape(context.stringValue()) + "\t");
		}
		Resource subj = st.getSubject();
		if (subj instanceof BNode) {
			throw new RuntimeException("Unexpected blank node");
		} else {
			sb.append(escape(subj.stringValue()) + "\t");
		}
		URI pred = st.getPredicate();
		sb.append(escape(pred.stringValue()) + "\t");
		Value obj = st.getObject();
		if (obj instanceof BNode) {
			throw new RuntimeException("Unexpected blank node");
		} else if (obj instanceof Literal) {
			sb.append("\t");
			Literal objl = (Literal) obj;
			if (objl.getDatatype() == null) {
				sb.append(" ");
			} else {
				sb.append(escape(objl.getDatatype().stringValue()) + " ");
			}
			if (objl.getLanguage() == null) {
				sb.append(" ");
			} else {
				sb.append(escape(objl.getLanguage()) + " ");
			}
			sb.append(escape(obj.stringValue()));
		} else {
			sb.append(escape(obj.stringValue()));
		}
		return sb.toString();
	}

	private static final String escape(String s) {
		return s.replaceAll("\\\\", "\\\\\\\\").replaceAll("\\n", "\\\\n").replaceAll("\\t", "\\\\t");
	}

	private static final String unescape(String s) {
		return s.replaceAll("\\\\t", "\t").replaceAll("\\\\n", "\n").replaceAll("\\\\\\\\", "\\");
	}

}
