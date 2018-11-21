package com.justlibre.stow4j.local;

import java.util.ArrayList;
import java.util.List;

import com.justlibre.stow4j.Identifiable;
import com.justlibre.stow4j.Pair;
import com.justlibre.stow4j.Stow;
import com.justlibre.stow4j.StowException;

public class Paging {
	
	public static <E extends Identifiable> Pair<List<E>, String> page(List<E> all, String cursor, int count)
			throws StowException {
		List<E> pageElems = new ArrayList<>();
		
		// locate the cursor
		boolean copy = true;
		if (!Stow.isStartCursor(cursor)) {
			copy = false;
		}
		int i = 0;
		int pos = 0;
		for (E elem : all) {
			if (!copy && elem.id().equals(cursor)) {
				copy = true;
			}
			if (copy) {
				if (i < count) {
					pageElems.add(elem);
					i++;
				} else {
					break;
				}
			}
			pos++;
		}
		String newCursor = Stow.StartCursor;
		if (i >= count) {
			if (pos < all.size()) {
				newCursor = all.get(pos).id();
			} else {
				//finished
			}
		}
		if (!Stow.isStartCursor(cursor) && pageElems.size() == 0) {
			throw new StowException("bad cursor");
		}

		return new Pair<>(pageElems, newCursor);
	}
}
