package com.gwtt.simulator.netconf.subsystem;

public enum NetconfMessageState {

	NO_MATCHING_PATTERN {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == ']') {
				return FIRST_BRACKET;
			} else if (c == '\n') {
				return FIRST_LF;
			} else {
				return this;
			}
		}
	},
	FIRST_BRACKET {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == ']') {
				return SECOND_BRACKET;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	SECOND_BRACKET {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == '>') {
				return FIRST_BIGGER;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	FIRST_BIGGER {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == ']') {
				return THIRD_BRACKET;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	THIRD_BRACKET {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == ']') {
				return ENDING_BIGGER;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	ENDING_BIGGER {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == '>') {
				return END_PATTERN;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	FIRST_LF {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == '#') {
				return FIRST_HASH;
			} else if (c == ']') {
				return FIRST_BRACKET;
			} else if (c == '\n') {
				return this;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	FIRST_HASH {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == '#') {
				return SECOND_HASH;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	SECOND_HASH {
		@Override
		NetconfMessageState evaluateChar(char c) {
			if (c == '\n') {
				return END_CHUNKED_PATTERN;
			} else {
				return NO_MATCHING_PATTERN;
			}
		}
	},
	END_CHUNKED_PATTERN {
		@Override
		NetconfMessageState evaluateChar(char c) {
			return NO_MATCHING_PATTERN;
		}
	},
	END_PATTERN {
		@Override
		NetconfMessageState evaluateChar(char c) {
			return NO_MATCHING_PATTERN;
		}
	};

	abstract NetconfMessageState evaluateChar(char c);
}
