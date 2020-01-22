/*******************************************************************************
 * Copyright (c) 2001-2014 Yann-Ga�l Gu�h�neuc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v2.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     Yann-Ga�l Gu�h�neuc and others, see in file; API and its implementation
 ******************************************************************************/

%-----------------------------------------------------------------------------

nextRelationCheck :-
	nextMethodEntryEvent(['fr.emn.cacao.Relation'], _, METHODNAME, _, _, CLASSNAME),
	METHODNAME = check,
	% write(CLASSNAME),
	% write('.'),
	% write(METHODNAME),
	% write('()'), nl,
	true.

nextVariableRevise :-
	nextMethodEntryEvent(['fr.emn.cacao.Variable'], _, METHODNAME, _, _, CLASSNAME),
	METHODNAME = revise,
	% write(CLASSNAME),
	% write('.'),
	% write(METHODNAME),
	% write('()'), nl,
	true.

query(N, M) :-
	nextRelationCheck,
	nextVariableRevise,
	N1 is N + 1,
	write(N1), nl,
	query(N1, M).
query(N, N).
	

main(N, M) :-
	ms(query(N, M), TIME),
	write(TIME),
	write(' ms.'),
	nl.
main(N, N).
