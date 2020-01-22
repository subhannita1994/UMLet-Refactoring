% (c) Copyright 2001 Yann-Ga�l Gu�h�neuc
% Ecole des Mines de Nantes and Object Technology Internationl, Inc.
% 
% Use and copying of this software and preparation of derivative works
% based upon this software are permitted. Any copy of this software or
% of any derivative work must include the above copyright notice of
% Yann-Ga�l Gu�h�neuc, this paragraph and the one after it.
% 
% This software is made available AS IS, and THE AUTHOR DISCLAIMS
% ALL WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
% IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
% PURPOSE, AND NOT WITHSTANDING ANY OTHER PROVISION CONTAINED HEREIN, ANY
% LIABILITY FOR DAMAGES RESULTING FROM THE SOFTWARE OR ITS USE IS
% EXPRESSLY DISCLAIMED, WHETHER ARISING IN CONTRACT, TORT (INCLUDING
% NEGLIGENCE) OR STRICT LIABILITY, EVEN IF YANN-GAEL GUEHENEUC IS ADVISED
% OF THE POSSIBILITY OF SUCH DAMAGES.
% 
% All Rights Reserved.

%-----------------------------------------------------------------------------

query :-
	nextEvent(
		[generateClassLoadEvent,
		 generateClassUnloadEvent,
		 generateConstructorEntryEvent,
		 generateConstructorExitEvent,
		 generateFieldAccessEvent,
		 generateFieldModificationEvent,
		 generateFinalizerEntryEvent,
		 generateFinalizerExitEvent,
		 generateMethodEntryEvent,
		 generateMethodExitEvent,
		 generateMethodReturnedValueEvent,
		 generateProgramEndEvent],
		E),
	write(E), nl, fail.

main(N1, N2) :- query.
main(N1, N1).
